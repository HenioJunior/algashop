package com.henio.algashop.ordering.infrastructure.persistence.disassembler;

import com.henio.algashop.ordering.domain.model.entity.Order;
import com.henio.algashop.ordering.domain.model.entity.OrderStatus;
import com.henio.algashop.ordering.domain.model.entity.PaymentMethod;
import com.henio.algashop.ordering.domain.model.valueobject.*;
import com.henio.algashop.ordering.domain.model.valueobject.id.CustomerId;
import com.henio.algashop.ordering.domain.model.valueobject.id.OrderId;
import com.henio.algashop.ordering.infrastructure.persistence.embeddable.AddressEmbeddable;
import com.henio.algashop.ordering.infrastructure.persistence.embeddable.BillingEmbeddable;
import com.henio.algashop.ordering.infrastructure.persistence.embeddable.RecipientEmbeddable;
import com.henio.algashop.ordering.infrastructure.persistence.embeddable.ShippingEmbeddable;
import com.henio.algashop.ordering.infrastructure.persistence.entity.OrderPersistenceEntity;
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
                .customerId(
                        new CustomerId(
                                TSID.from(
                                        Objects.requireNonNull(
                                                persistenceEntity.getCustomerId(),
                                                "Customer id is required"
                                        )
                                )
                        )
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

    private Shipping toShippingValueObject(ShippingEmbeddable shippingEmbeddable) {
        RecipientEmbeddable recipientEmbeddable = shippingEmbeddable.getRecipient();
        return Shipping.builder()
                .cost(new Money(shippingEmbeddable.getCost()))
                .expectedDate(shippingEmbeddable.getExpectedDate())
                .recipient(
                        Recipient.builder()
                                .fullName(new FullName(recipientEmbeddable.getFirstName(), recipientEmbeddable.getLastName()))
                                .document(new Document(recipientEmbeddable.getDocument()))
                                .phone(new Phone(recipientEmbeddable.getPhone()))
                                .build()
                )
                .address(toAddressValueObject(shippingEmbeddable.getAddress()))
                .build();
    }

    private Billing toBillingValueObject(BillingEmbeddable billingEmbeddable) {
        return Billing.builder()
                .fullName(new FullName(billingEmbeddable.getFirstName(), billingEmbeddable.getLastName()))
                .document(new Document(billingEmbeddable.getDocument()))
                .phone(new Phone(billingEmbeddable.getPhone()))
                .address(toAddressValueObject(billingEmbeddable.getAddress()))
                .build();
    }

    private Address toAddressValueObject(AddressEmbeddable address) {
        return Address.builder()
                .street(address.getStreet())
                .number(address.getNumber())
                .complement(address.getComplement())
                .neighborhood(address.getNeighborhood())
                .city(address.getCity())
                .state(address.getState())
                .zipCode(new ZipCode(address.getZipCode()))
                .build();
    }
}
