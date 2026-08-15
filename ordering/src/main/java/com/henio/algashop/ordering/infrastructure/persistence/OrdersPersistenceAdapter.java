package com.henio.algashop.ordering.infrastructure.persistence;

import com.henio.algashop.ordering.domain.model.entity.Order;
import com.henio.algashop.ordering.domain.model.exception.OrderNotFoundException;
import com.henio.algashop.ordering.domain.model.repository.Orders;
import com.henio.algashop.ordering.domain.model.valueobject.id.OrderId;
import org.springframework.stereotype.Component;

import java.util.Objects;

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
    public Order ofId(OrderId orderId) {
        Objects.requireNonNull(orderId, "Order id is required");
        OrderPersistenceEntity orderPersistenceEntity = repository.findById(orderId.value().toLong())
                .orElseThrow(() -> new OrderNotFoundException(orderId));
        return disassembler.toDomain(orderPersistenceEntity);
    }


    @Override
    public void add(Order aggregateRoot) {
        Objects.requireNonNull(aggregateRoot, "Order is required");
        repository.saveAndFlush(assembler.fromDomain(aggregateRoot));
    }
}
