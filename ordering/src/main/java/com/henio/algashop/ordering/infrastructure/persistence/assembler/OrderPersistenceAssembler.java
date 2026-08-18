package com.henio.algashop.ordering.infrastructure.persistence.assembler;

import com.henio.algashop.ordering.domain.model.entity.Order;
import com.henio.algashop.ordering.domain.model.valueobject.Address;
import com.henio.algashop.ordering.domain.model.valueobject.Billing;
import com.henio.algashop.ordering.domain.model.valueobject.Recipient;
import com.henio.algashop.ordering.domain.model.valueobject.Shipping;
import com.henio.algashop.ordering.infrastructure.persistence.embeddable.AddressEmbeddable;
import com.henio.algashop.ordering.infrastructure.persistence.embeddable.BillingEmbeddable;
import com.henio.algashop.ordering.infrastructure.persistence.embeddable.RecipientEmbeddable;
import com.henio.algashop.ordering.infrastructure.persistence.embeddable.ShippingEmbeddable;
import com.henio.algashop.ordering.infrastructure.persistence.entity.OrderPersistenceEntity;
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
        entity.setBilling(toBillingEmbeddable(order.billing()));
        entity.setShipping(toShippingEmbeddable(order.shipping()));

        return entity;
    }

    private BillingEmbeddable toBillingEmbeddable(Billing billing) {
        if (billing == null) {
            return null;
        }
        return BillingEmbeddable.builder()
                .firstName(billing.fullName().firstName())
                .lastName(billing.fullName().lastName())
                .document(billing.document().value())
                .phone(billing.phone().value())
                .address(toAddressEmbeddable(billing.address()))
                .build();
    }

    private AddressEmbeddable toAddressEmbeddable(Address address) {
        if (address == null) {
            return null;
        }
        return AddressEmbeddable.builder()
                .city(address.city())
                .state(address.state())
                .number(address.number())
                .street(address.street())
                .complement(address.complement())
                .neighborhood(address.neighborhood())
                .zipCode(address.zipCode().value())
                .build();
    }

    private ShippingEmbeddable toShippingEmbeddable(Shipping shipping) {
        if (shipping == null) {
            return null;
        }
        var builder = ShippingEmbeddable.builder()
                .expectedDate(shipping.expectedDate())
                .cost(shipping.cost().value())
                .address(toAddressEmbeddable(shipping.address()));
        Recipient recipient = shipping.recipient();
        if (recipient != null) {
            builder.recipient(
                    RecipientEmbeddable.builder()
                            .firstName(recipient.fullName().firstName())
                            .lastName(recipient.fullName().lastName())
                            .document(recipient.document().value())
                            .phone(recipient.phone().value())
                            .build()
            );
        }
        return builder.build();
    }

}
