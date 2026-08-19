package com.henio.algashop.ordering.infrastructure.persistence.entity;

import com.henio.algashop.ordering.domain.model.utility.IdGenerator;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.Set;

public class OrderPersistenceEntityTestDataBuilder {

    private OrderPersistenceEntityTestDataBuilder() {
    }

    public static OrderPersistenceEntity existingOrder() {

        OrderPersistenceEntity order =
        OrderPersistenceEntity.builder()
                .id(IdGenerator.generateTSID().toLong())
                .customerId(IdGenerator.generateTSID().toLong())
                .totalItems(3)
                .totalAmount(new BigDecimal(1250))
                .status("DRAFT")
                .paymentMethod("CREDIT_CARD")
                .placedAt(OffsetDateTime.now())
                .build();

        Set<OrderItemPersistenceEntity> items = Set.of(existingOrderItem(), existingOrderItemAlt());

        order.replaceItems(items);

        return order;
    }

    public static OrderItemPersistenceEntity existingOrderItem() {
        return OrderItemPersistenceEntity.builder()
                .id(IdGenerator.generateTSID().toLong())
                .productId(IdGenerator.generateTSID().toLong())
                .productName("Notebook")
                .price(new BigDecimal(500))
                .quantity(2)
                .totalAmount(new BigDecimal(1000))
                .build();
    }

    public static OrderItemPersistenceEntity existingOrderItemAlt() {
        return OrderItemPersistenceEntity.builder()
                .id(IdGenerator.generateTSID().toLong())
                .productId(IdGenerator.generateTSID().toLong())
                .productName("iPhone")
                .price(new BigDecimal(10000))
                .quantity(1)
                .totalAmount(new BigDecimal(10000))
                .build();
    }
}
