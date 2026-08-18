package com.henio.algashop.ordering.infrastructure.persistence;

import com.henio.algashop.ordering.domain.model.entity.Order;
import com.henio.algashop.ordering.domain.model.exception.OrderNotFoundException;
import com.henio.algashop.ordering.domain.model.repository.Orders;
import com.henio.algashop.ordering.domain.model.valueobject.id.OrderId;
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

    private void insert(Order aggregateRoot) {
        OrderPersistenceEntity orderPersistenceEntity = assembler.fromDomain(aggregateRoot);
        repository.save(orderPersistenceEntity);
    }

    private void update(Order aggregateRoot, OrderPersistenceEntity orderPersistenceEntity) {
        OrderPersistenceEntity updatedEntity = assembler.merge(orderPersistenceEntity, aggregateRoot);
        repository.save(updatedEntity);
    }
}
