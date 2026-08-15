package com.henio.algashop.ordering.infrastructure.persistence;

import com.henio.algashop.ordering.domain.model.entity.Order;
import com.henio.algashop.ordering.domain.model.entity.OrderStatus;
import com.henio.algashop.ordering.domain.model.entity.PaymentMethod;
import com.henio.algashop.ordering.domain.model.valueobject.Money;
import com.henio.algashop.ordering.domain.model.valueobject.Quantity;
import com.henio.algashop.ordering.domain.model.valueobject.id.CustomerId;
import com.henio.algashop.ordering.domain.model.valueobject.id.OrderId;
import io.hypersistence.tsid.TSID;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class OrderPersistenceDisassemblerTest {

    private final OrderPersistenceDisassembler disassembler = new OrderPersistenceDisassembler();

    @Test
    void shouldConvertFromPersistence() {
        OrderPersistenceEntity persistenceEntity = OrderPersistenceEntityTestDataBuilder.existingOrder();
        Order domainEntity = disassembler.toDomain(persistenceEntity);
        assertThat(domainEntity).satisfies(
                s -> assertThat(s.id()).isEqualTo(new OrderId(TSID.from(persistenceEntity.getId()))),
                s -> assertThat(s.customerId()).isEqualTo(new CustomerId(TSID.from(persistenceEntity.getCustomerId()))),
                s -> assertThat(s.totalAmount()).isEqualTo(new Money(persistenceEntity.getTotalAmount())),
                s -> assertThat(s.totalItems()).isEqualTo(new Quantity(persistenceEntity.getTotalItems())),
                s -> assertThat(s.placedAt()).isEqualTo(persistenceEntity.getPlacedAt()),
                s -> assertThat(s.paidAt()).isEqualTo(persistenceEntity.getPaidAt()),
                s -> assertThat(s.canceledAt()).isEqualTo(persistenceEntity.getCanceledAt()),
                s -> assertThat(s.readyAt()).isEqualTo(persistenceEntity.getReadyAt()),
                s -> assertThat(s.status()).isEqualTo(OrderStatus.valueOf(persistenceEntity.getStatus())),
                s -> assertThat(s.paymentMethod()).isEqualTo(PaymentMethod.valueOf(persistenceEntity.getPaymentMethod()))
        );

    }
}