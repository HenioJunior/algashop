package com.henio.algashop.ordering.infrastructure.persistence.order;

import com.henio.algashop.ordering.application.order.query.*;
import com.henio.algashop.ordering.application.utility.Mapper;
import com.henio.algashop.ordering.domain.model.order.OrderId;
import com.henio.algashop.ordering.domain.model.order.exception.OrderNotFoundException;
import io.hypersistence.tsid.TSID;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;

@Component
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrderQueryServiceImpl implements OrderQueryService {

    private final OrderPersistenceEntityRepository repository;
    private final Mapper mapper;

    private final EntityManager entityManager;

    @Override
    public OrderDetailOutput findById(String orderId) {
        long id = new OrderId(orderId).value().toLong();
        OrderPersistenceEntity entity = repository.findById(id).orElseThrow(
                () -> new OrderNotFoundException(new OrderId(TSID.from(id)))
        );
        return mapper.convert(entity, OrderDetailOutput.class);
    }

    @Override
    public Page<OrderSummaryOutput> filter(OrderFilter filter) {
        Long totalQueryResult = countTotalQueryResults(filter);

        if(totalQueryResult.equals(0L)) {
            PageRequest pageRequest = PageRequest.of(filter.getPage(), filter.getSize());
            return new PageImpl<>(new ArrayList<>(), pageRequest, totalQueryResult);
        }
        return filterQuery(filter, totalQueryResult);
    }

    private Long countTotalQueryResults(OrderFilter filter) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> criteriaQuery = builder.createQuery(Long.class);
        Root<OrderPersistenceEntity> root = criteriaQuery.from(OrderPersistenceEntity.class);

        Expression<Long> count = builder.count(root);

        Predicate[] predicates = toPredicates(builder, root, filter);

        criteriaQuery.select(count);
        criteriaQuery.where(predicates);

        TypedQuery<Long> query = entityManager.createQuery(criteriaQuery);

        return query.getSingleResult();
    }

    private Page<OrderSummaryOutput> filterQuery(OrderFilter filter, Long totalQueryResults) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<OrderSummaryOutput> criteriaQuery = builder.createQuery(OrderSummaryOutput.class);
        Root<OrderPersistenceEntity> root = criteriaQuery.from(OrderPersistenceEntity.class);

        Path<Object> customer = root.get("customer");

        criteriaQuery.select(
                builder.construct(OrderSummaryOutput.class,
                        root.get("id"),
                        builder.construct(CustomerMinimalOutput.class,
                                customer.get("id"),
                                customer.get("firstName"),
                                customer.get("lastName"),
                                customer.get("email"),
                                customer.get("document"),
                                customer.get("phone")
                        ),
                        root.get("totalItems"),
                        root.get("totalAmount"),
                        root.get("placedAt"),
                        root.get("paidAt"),
                        root.get("canceledAt"),
                        root.get("readyAt"),
                        root.get("status"),
                        root.get("paymentMethod")
                )
        );

        Predicate[] predicates = toPredicates(builder, root, filter);

        criteriaQuery.where(predicates);

        TypedQuery<OrderSummaryOutput> typedQuery = entityManager.createQuery(criteriaQuery);

        typedQuery.setFirstResult(filter.getSize() * filter.getPage());
        typedQuery.setMaxResults(filter.getSize());

        PageRequest pageRequest = PageRequest.of(filter.getPage(), filter.getSize());

        return new PageImpl<>(typedQuery.getResultList(), pageRequest, totalQueryResults);
    }

    private Predicate[] toPredicates(
            CriteriaBuilder builder,
            Root<OrderPersistenceEntity> root,
            OrderFilter filter
    ) {
        ArrayList<Predicate> predicates = new ArrayList<>();

        if(filter.getStatus() != null && !filter.getStatus().isBlank()) {
            Path<Object> statusPath = root.get("status");
            Predicate predicate = builder.equal(statusPath, filter.getStatus().toUpperCase());
            predicates.add(predicate);
        }

        if(filter.getOrderId() != null) {
            long orderIdLongValue;

            try {
                OrderId orderId = new OrderId(filter.getOrderId());
                orderIdLongValue = orderId.value().toLong();
            } catch (IllegalArgumentException e) {
                orderIdLongValue = 0L;
            }
            predicates.add(builder.equal(root.get("id"), orderIdLongValue));
        }

        if(filter.getCustomerId() != null) {
            Path<Object> customerIdPath = root.get("customer").get("id");
            Long expectedCustomerId = TSID.from(filter.getCustomerId()).toLong();
            Predicate predicate = builder.equal(customerIdPath, expectedCustomerId);
            predicates.add(predicate);
        }

        if(filter.getPlacedAtFrom() != null) {
            predicates.add(builder.greaterThanOrEqualTo(root.get("placedAt"), filter.getPlacedAtFrom()));
        }

        if (filter.getPlacedAtTo() != null) {
            predicates.add(builder.lessThanOrEqualTo(root.get("placedAt"), filter.getPlacedAtTo()));
        }

        if (filter.getTotalAmountFrom() != null) {
            predicates.add(builder.greaterThanOrEqualTo(root.get("totalAmount"), filter.getTotalAmountFrom()));
        }

        if (filter.getTotalAmountTo() != null) {
            predicates.add(builder.lessThanOrEqualTo(root.get("totalAmount"), filter.getTotalAmountTo()));
        }

        return predicates.toArray(new Predicate[]{});
    }
}
