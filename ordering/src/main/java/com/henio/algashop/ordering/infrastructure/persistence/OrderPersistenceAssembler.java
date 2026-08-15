package com.henio.algashop.ordering.infrastructure.persistence;

import com.henio.algashop.ordering.domain.model.entity.Order;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class OrderPersistenceAssembler {

    public OrderPersistenceEntity fromDomain(Order order) {
        Objects.requireNonNull(order, "Order is required");

        return merge(new OrderPersistenceEntity(), order);
    }

    public OrderPersistenceEntity merge(
            OrderPersistenceEntity entity,
            Order order
    ) {
        Objects.requireNonNull(entity, "Order persistence entity is required");
        Objects.requireNonNull(order, "Order is required");

        entity.setId(order.id().value().toLong());
        entity.setCustomerId(order.customerId().value().toLong());
        entity.setTotalAmount(order.totalAmount().value());
        entity.setTotalItems(order.totalItems().value());
        entity.setStatus(order.status().name());
        entity.setPaymentMethod(
                order.paymentMethod() == null
                        ? null
                        : order.paymentMethod().name()
        );

        entity.setPlacedAt(order.placedAt());
        entity.setPaidAt(order.paidAt());
        entity.setCanceledAt(order.canceledAt());
        entity.setReadyAt(order.readyAt());

        return entity;
    }
}
