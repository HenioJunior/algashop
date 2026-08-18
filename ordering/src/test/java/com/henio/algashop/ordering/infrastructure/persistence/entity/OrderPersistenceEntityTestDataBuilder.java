package com.henio.algashop.ordering.infrastructure.persistence.entity;

import com.henio.algashop.ordering.domain.model.utility.IdGenerator;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

public class OrderPersistenceEntityTestDataBuilder {

    private OrderPersistenceEntityTestDataBuilder() {
    }

    public static OrderPersistenceEntity existingOrder() {
        return OrderPersistenceEntity.builder()
                .id(IdGenerator.generateTSID().toLong())
                .customerId(IdGenerator.generateTSID().toLong())
                .totalItems(3)
                .totalAmount(new BigDecimal(1250))
                .status("DRAFT")
                .paymentMethod("CREDIT_CARD")
                .placedAt(OffsetDateTime.now())
                .build();
    }
}
