package com.henio.algashop.ordering.infrastructure.persistence.customer;

import com.henio.algashop.ordering.application.customer.query.CustomerFilter;
import com.henio.algashop.ordering.application.customer.query.CustomerOutput;
import com.henio.algashop.ordering.application.customer.query.CustomerQueryService;
import com.henio.algashop.ordering.application.customer.query.CustomerSummaryOutput;
import com.henio.algashop.ordering.domain.model.customer.CustomerId;
import com.henio.algashop.ordering.domain.model.customer.exception.CustomerNotFoundException;
import io.hypersistence.tsid.TSID;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CustomerQueryServiceImpl implements CustomerQueryService {

    private final EntityManager entityManager;

    private static final String findByIdAsOutputJPQL = """
            SELECT new com.henio.algashop.ordering.application.customer.query.CustomerOutput(
                c.id,
                c.firstName,
                c.lastName,
                c.email,
                c.document,
                c.phone,
                c.birthDate,
                c.loyaltyPoints,
                c.registeredAt,
                c.archivedAt,
                c.promotionNotificationsAllowed,
                c.archived,
                new com.henio.algashop.ordering.application.commons.AddressData(
                    c.address.street,
                    c.address.number,
                    c.address.complement,
                    c.address.neighborhood,
                    c.address.city,
                    c.address.state,
                    c.address.zipCode
                )
            )
            FROM CustomerPersistenceEntity c
            WHERE c.id = :id""";

    @Override
    public CustomerOutput findById(String customerId) {
        long id = TSID.from(customerId).toLong();
        try {
            TypedQuery<CustomerOutput> query = entityManager.createQuery(findByIdAsOutputJPQL, CustomerOutput.class);
            query.setParameter("id", id);
            return query.getSingleResult();
        } catch (NoResultException e) {
            throw new CustomerNotFoundException(new CustomerId(TSID.from(customerId)));
        }
    }

    @Override
    public Page<CustomerSummaryOutput> filter(CustomerFilter filter) {
        Long totalQueryResult = countTotalQueryResults(filter);
        if(totalQueryResult.equals(0L)) {
            PageRequest pageRequest = PageRequest.of(filter.getPage(), filter.getSize());
            return Page.empty(pageRequest);
        }
        return filterQuery(filter, totalQueryResult);
    }

    private Long countTotalQueryResults(CustomerFilter filter) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> criteriaQuery = builder.createQuery(Long.class);
        Root<CustomerPersistenceEntity> root = criteriaQuery.from(CustomerPersistenceEntity.class);

        Expression<Long> count = builder.count(root);

        criteriaQuery.select(count);
        criteriaQuery.where(
                toPredicates(builder, root, filter)
        );

        return entityManager.createQuery(criteriaQuery).getSingleResult();
    }

    private Page<CustomerSummaryOutput> filterQuery(CustomerFilter filter, Long totalQueryResult) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<CustomerSummaryOutput> criteriaQuery = builder.createQuery(CustomerSummaryOutput.class);
        Root<CustomerPersistenceEntity> root = criteriaQuery.from(CustomerPersistenceEntity.class);

        criteriaQuery.select(
                builder.construct(
                        CustomerSummaryOutput.class,
                        root.get("firstName"),
                        root.get("email")
                )
        );

        criteriaQuery.where(
                toPredicates(builder, root, filter)
        );
        Order sortOrder = toOrder(builder, root, filter);

        if(sortOrder != null) {
            criteriaQuery.orderBy(sortOrder);
        }

        TypedQuery<CustomerSummaryOutput> typedQuery = entityManager
                .createQuery(criteriaQuery);

        typedQuery.setFirstResult(filter.getSize() * filter.getPage());
        typedQuery.setMaxResults(filter.getSize());

        PageRequest pageRequest = PageRequest.of(filter.getPage(), filter.getSize());

        return new PageImpl<>(typedQuery.getResultList(), pageRequest, totalQueryResult);
    }

    private Predicate[] toPredicates(
            CriteriaBuilder builder,
            Root<CustomerPersistenceEntity> root,
            CustomerFilter filter
    ) {
        List<Predicate> predicates = new ArrayList<>();

        if(filter.getFirstName() != null && !filter.getFirstName().isBlank()) {
            predicates.add(
                    builder.like(
                            builder.lower(root.get("firstName")),
                            "%" + filter.getFirstName().toLowerCase() + "%"
                    )
            );
        }

        if(filter.getEmail() != null && !filter.getEmail().isBlank()) {
            predicates.add(
                    builder.like(
                            builder.lower(root.get("email")),
                            "%" + filter.getEmail().toLowerCase() + "%"
                    )
            );
        }

        return predicates.toArray(Predicate[]::new);
    }

    private Order toOrder(
            CriteriaBuilder builder,
            Root<CustomerPersistenceEntity> root,
            CustomerFilter filter
    ) {
        var property = root.get(filter.getSortByPropertyOrDefault().getPropertyName());

        if(filter.getSortDirectionOrDefault() == Sort.Direction.ASC) {
            return builder.asc(property);
        }
        return builder.desc(property);
    }
}