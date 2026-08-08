package com.henio.algashop.ordering.domain.model.entity;

import com.henio.algashop.ordering.domain.model.exception.OrderStatusCannotBeChangedException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import java.time.OffsetDateTime;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

public class OrderMarkAsReadyTest {

    @Test
    void givenPaidOrder_whenMarkAsReady_shouldChangeStatusAndSetReadyAt() {
        Order order = OrderTestDataBuilder.anOrder()
                .status(OrderStatus.PAID)
                .build();

        OffsetDateTime before = OffsetDateTime.now();

        order.markAsReady();

        OffsetDateTime after = OffsetDateTime.now();

        assertThat(order.status())
                .isEqualTo(OrderStatus.READY);

        assertThat(order.readyAt())
                .isNotNull()
                .isBetween(before, after);
    }

    @ParameterizedTest
    @EnumSource(
            value = OrderStatus.class,
            names = {"DRAFT", "PLACED", "CANCELED"})
    void givenOrderNotPaid_whenMarkAsReady_shouldThrowExceptionAndKeepReadyAtNull(OrderStatus currentStatus) {

        Order order = OrderTestDataBuilder.anOrder()
                .status(currentStatus)
                .build();

        assertThatThrownBy(order::markAsReady)
                .isInstanceOf(OrderStatusCannotBeChangedException.class);

        assertThat(order.status())
                .isEqualTo(currentStatus);

        assertThat(order.readyAt())
                .isNull();
    }

    @Test
    void givenReadyOrder_whenMarkAsReady_shouldThrowExceptionAndKeepOriginalReadyAt() {
        Order order = OrderTestDataBuilder.anOrder()
                .status(OrderStatus.READY)
                .build();

        OffsetDateTime originalReadyAt = order.readyAt();

        assertThatThrownBy(order::markAsReady)
                .isInstanceOf(OrderStatusCannotBeChangedException.class);

        assertThat(order.status())
                .isEqualTo(OrderStatus.READY);

        assertThat(order.readyAt())
                .isEqualTo(originalReadyAt);
    }
}
