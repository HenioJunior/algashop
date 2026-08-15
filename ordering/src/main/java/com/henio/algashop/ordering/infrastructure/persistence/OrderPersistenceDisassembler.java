package com.henio.algashop.ordering.infrastructure.persistence;

import com.henio.algashop.ordering.domain.model.entity.Order;
import com.henio.algashop.ordering.domain.model.entity.OrderStatus;
import com.henio.algashop.ordering.domain.model.entity.PaymentMethod;
import com.henio.algashop.ordering.domain.model.valueobject.Money;
import com.henio.algashop.ordering.domain.model.valueobject.Quantity;
import com.henio.algashop.ordering.domain.model.valueobject.id.CustomerId;
import com.henio.algashop.ordering.domain.model.valueobject.id.OrderId;
import io.hypersistence.tsid.TSID;
import org.springframework.stereotype.Component;

import java.util.HashSet;

@Component
public class OrderPersistenceDisassembler {

    public Order toDomain(OrderPersistenceEntity persistenceEntity) {
        return Order.existing()
                .id(new OrderId(TSID.from(persistenceEntity.getId())))
                .customerId(new CustomerId(TSID.from(persistenceEntity.getCustomerId())))
                .totalAmount(new Money(persistenceEntity.getTotalAmount()))
                .totalItems(new Quantity(persistenceEntity.getTotalItems()))
                .placedAt(persistenceEntity.getPlacedAt())
                .paidAt(persistenceEntity.getPaidAt())
                .canceledAt(persistenceEntity.getCanceledAt())
                .readyAt(persistenceEntity.getReadyAt())
                .status(OrderStatus.valueOf(persistenceEntity.getStatus()))
                .paymentMethod( persistenceEntity.getPaymentMethod() == null
                        ? null
                        : PaymentMethod.valueOf(
                        persistenceEntity.getPaymentMethod()
                ))
                .items(new HashSet<>())
                .build();
    }
}
