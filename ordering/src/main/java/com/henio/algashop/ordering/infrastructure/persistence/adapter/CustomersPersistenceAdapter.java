package com.henio.algashop.ordering.infrastructure.persistence.adapter;

import com.henio.algashop.ordering.domain.model.entity.Customer;
import com.henio.algashop.ordering.domain.model.repository.Customers;
import com.henio.algashop.ordering.domain.model.valueobject.Email;
import com.henio.algashop.ordering.domain.model.valueobject.id.CustomerId;
import com.henio.algashop.ordering.infrastructure.persistence.AggregateVersionUpdater;
import com.henio.algashop.ordering.infrastructure.persistence.assembler.CustomerPersistenceAssembler;
import com.henio.algashop.ordering.infrastructure.persistence.disassembler.CustomerPersistenceDisassembler;
import com.henio.algashop.ordering.infrastructure.persistence.entity.CustomerPersistenceEntity;
import com.henio.algashop.ordering.infrastructure.persistence.repository.CustomerPersistenceEntityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.Optional;

@Transactional(readOnly = true)
@Component
@RequiredArgsConstructor
public class CustomersPersistenceAdapter implements Customers {

    private final CustomerPersistenceEntityRepository persistenceRepository;
    private final CustomerPersistenceAssembler assembler;
    private final CustomerPersistenceDisassembler disassembler;

    @Override
    public Optional<Customer> ofId(CustomerId customerId) {
        Optional<CustomerPersistenceEntity> possibleCustomer = persistenceRepository.findById(customerId.value().toLong());
        return possibleCustomer.map(disassembler::toDomain);
    }

    @Transactional(readOnly = false)
    @Override
    public void add(Customer aggregateRoot) {
        Objects.requireNonNull(
                aggregateRoot,
                "Customer must be not null"
        );

        long customerId = aggregateRoot.id()
                .value()
                .toLong();

        persistenceRepository.findById(customerId)
                .ifPresentOrElse(entity -> update(aggregateRoot, entity),
                        () -> insert(aggregateRoot));

    }

    private void update(Customer aggregateRoot, CustomerPersistenceEntity entity) {
        checkVersion(aggregateRoot, entity);
        assembler.merge(entity, aggregateRoot);

        persistenceRepository.flush();

        AggregateVersionUpdater.update(
                aggregateRoot,
                entity.getVersion()
        );
    }

    private void insert(Customer aggregateRoot) {
        CustomerPersistenceEntity entity = assembler.fromDomain(aggregateRoot);
        persistenceRepository.saveAndFlush(entity);
        AggregateVersionUpdater.update(
                aggregateRoot,
                entity.getVersion()
        );
    }

    private void checkVersion(
            Customer aggregateRoot,
            CustomerPersistenceEntity entity
    ) {
        if (!Objects.equals(
                aggregateRoot.version(),
                entity.getVersion()
        )) {
            throw new ObjectOptimisticLockingFailureException(
                    Customer.class,
                    aggregateRoot.id()
            );
        }
    }

    @Override
    public long count() {
        return persistenceRepository.count();
    }

    @Override
    public boolean exists(CustomerId customerId) {
        return persistenceRepository.existsById(customerId.value().toLong());
    }

    @Override
    public Optional<Customer> ofEmail(Email email) {
        return persistenceRepository.findByEmail(email.value())
                .map(disassembler::toDomain);
    }
}
