package com.henio.algashop.ordering.infrastructure.persistence.adapter;

import com.henio.algashop.ordering.domain.model.entity.Order;
import com.henio.algashop.ordering.domain.model.repository.Orders;
import com.henio.algashop.ordering.domain.model.valueobject.id.OrderId;
import com.henio.algashop.ordering.infrastructure.persistence.AggregateVersionUpdater;
import com.henio.algashop.ordering.infrastructure.persistence.repository.OrderPersistenceEntityRepository;
import com.henio.algashop.ordering.infrastructure.persistence.assembler.OrderPersistenceAssembler;
import com.henio.algashop.ordering.infrastructure.persistence.disassembler.OrderPersistenceDisassembler;
import com.henio.algashop.ordering.infrastructure.persistence.entity.OrderPersistenceEntity;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.Optional;

@Component
public class OrdersPersistenceAdapter implements Orders {

    private final OrderPersistenceEntityRepository repository;
    private final OrderPersistenceAssembler assembler;
    private final OrderPersistenceDisassembler disassembler;

    public OrdersPersistenceAdapter(OrderPersistenceEntityRepository repository, OrderPersistenceAssembler assembler,
                                    OrderPersistenceDisassembler disassembler) {
        this.repository = repository;
        this.assembler = assembler;
        this.disassembler = disassembler;
    }

    @Override
    public Optional<Order> ofId(OrderId orderId) {
        Optional<OrderPersistenceEntity> possibleEntity = repository.findById(orderId.value().toLong());
        return possibleEntity.map(disassembler::toDomain);
    }


    @Override
    public void add(Order aggregateRoot) {
        Objects.requireNonNull(
                aggregateRoot,
                "Order is required");

        long orderId = aggregateRoot.id()
                .value()
                .toLong();

        repository.findById(orderId)
                .ifPresentOrElse(
                        entity -> update(aggregateRoot, entity),
                        () -> insert(aggregateRoot));
    }

    @Override
    public long count() {
        return repository.count();
    }

    @Override
    public boolean exists(OrderId orderId) {
        return repository.existsById(orderId.value().toLong());
    }

    private void update(Order aggregateRoot, OrderPersistenceEntity entity) {
        checkVersion(aggregateRoot, entity);

        assembler.merge(entity, aggregateRoot);

        repository.flush();

        AggregateVersionUpdater.update(
                aggregateRoot,
                entity.getVersion()
        );
    }

    private void insert(Order aggregateRoot) {
        OrderPersistenceEntity entity = assembler.fromDomain(aggregateRoot);
        repository.saveAndFlush(entity);
        AggregateVersionUpdater.update(
                aggregateRoot,
                entity.getVersion()
        );
    }

    private void checkVersion(
            Order aggregateRoot,
            OrderPersistenceEntity entity
    ) {
        if (!Objects.equals(
                aggregateRoot.version(),
                entity.getVersion()
        )) {
            throw new ObjectOptimisticLockingFailureException(
                    Order.class,
                    aggregateRoot.id()
            );
        }
    }
}
