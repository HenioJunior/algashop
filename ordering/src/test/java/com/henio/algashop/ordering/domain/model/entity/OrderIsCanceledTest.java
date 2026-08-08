package com.henio.algashop.ordering.domain.model.entity;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class OrderIsCanceledTest {

    @Test
    void givenCanceledOrder_whenCheckIsCanceled_shouldReturnTrue() {
        Order order = OrderTestDataBuilder.anOrder()
                .status(OrderStatus.CANCELED)
                .build();

        assertThat(order.isCanceled())
                .isTrue();
    }

    @ParameterizedTest
    @EnumSource(
            value = OrderStatus.class,
            names = {"CANCELED"},
            mode = EnumSource.Mode.EXCLUDE
    )
    void givenOrderNotCanceled_whenCheckIsCanceled_shouldReturnFalse(
            OrderStatus currentStatus
    ) {
        Order order = OrderTestDataBuilder.anOrder()
                .status(currentStatus)
                .build();

        assertThat(order.isCanceled())
                .isFalse();
    }
}
