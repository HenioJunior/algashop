package com.henio.algashop.ordering.domain.entity;

import com.henio.algashop.ordering.domain.exception.OrderStatusCannotBeChangedException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import java.time.OffsetDateTime;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

class OrderCancelTest {

    @ParameterizedTest
    @EnumSource(
            value = OrderStatus.class,
            names = {"DRAFT", "PLACED", "PAID", "READY"}
    )
    void givenCancelableOrder_whenCancel_shouldChangeStatusAndSetCanceledAt(
            OrderStatus currentStatus
    ) {
        Order order = OrderTestDataBuilder.anOrder()
                .status(currentStatus)
                .build();

        OffsetDateTime beforeCancellation = OffsetDateTime.now();

        order.cancel();

        OffsetDateTime afterCancellation = OffsetDateTime.now();

        assertThat(order.status())
                .isEqualTo(OrderStatus.CANCELED);

        assertThat(order.canceledAt())
                .isNotNull()
                .isBetween(beforeCancellation, afterCancellation);
    }

    @Test
    void givenCanceledOrder_whenCancelAgain_shouldThrowExceptionAndKeepCanceledAt() {
        Order order = OrderTestDataBuilder.anOrder()
                .status(OrderStatus.CANCELED)
                .build();

        OffsetDateTime originalCanceledAt = order.canceledAt();

        assertThatThrownBy(order::cancel)
                .isInstanceOf(OrderStatusCannotBeChangedException.class);

        assertThat(order.status())
                .isEqualTo(OrderStatus.CANCELED);

        assertThat(order.canceledAt())
                .isEqualTo(originalCanceledAt);
    }
}
