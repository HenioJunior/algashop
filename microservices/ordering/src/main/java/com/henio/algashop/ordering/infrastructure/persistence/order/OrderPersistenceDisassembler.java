package com.henio.algashop.ordering.infrastructure.persistence.order;

import com.henio.algashop.ordering.domain.model.commons.Money;
import com.henio.algashop.ordering.domain.model.commons.Quantity;
import com.henio.algashop.ordering.domain.model.customer.CustomerId;
import com.henio.algashop.ordering.domain.model.order.Order;
import com.henio.algashop.ordering.domain.model.order.OrderId;
import com.henio.algashop.ordering.domain.model.order.OrderStatus;
import com.henio.algashop.ordering.domain.model.order.PaymentMethod;
import io.hypersistence.tsid.TSID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Objects;

@RequiredArgsConstructor
@Component
public class OrderPersistenceDisassembler {

    private final OrderItemPersistenceDisassembler itemDisassembler;

    public Order toDomain(OrderPersistenceEntity persistenceEntity) {
        return Order.existing()
                .id(new OrderId(TSID.from(persistenceEntity.getId())))
                .customerId(new CustomerId(Objects.requireNonNull(
                        persistenceEntity.getCustomerId(),"Customer id is required"))
                )
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
                .items(itemDisassembler.toDomain(persistenceEntity.getItems()))
                .version(persistenceEntity.getVersion())
                .build();
    }
}
