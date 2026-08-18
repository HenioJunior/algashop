package com.henio.algashop.ordering.domain.model.repository;

import com.henio.algashop.ordering.domain.model.entity.Order;
import com.henio.algashop.ordering.domain.model.entity.OrderStatus;
import com.henio.algashop.ordering.domain.model.entity.OrderTestDataBuilder;
import com.henio.algashop.ordering.domain.model.valueobject.id.OrderId;
import com.henio.algashop.ordering.infrastructure.persistence.OrderPersistenceAssembler;
import com.henio.algashop.ordering.infrastructure.persistence.OrderPersistenceDisassembler;
import com.henio.algashop.ordering.infrastructure.persistence.OrdersPersistenceAdapter;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.Optional;

@DataJpaTest
@Import({
        OrdersPersistenceAdapter.class,
        OrderPersistenceAssembler.class,
        OrderPersistenceDisassembler.class
})
class OrdersIT {

    private final Orders orders;

    @Autowired
    public OrdersIT(Orders orders) {
        this.orders = orders;
    }

    @Test
    void shouldPersistAndFind() {
        Order order = OrderTestDataBuilder.anOrder().build();
        OrderId orderId = order.id();

        orders.add(order);
        Optional<Order> savedOrder = orders.ofId(orderId);

        Assertions.assertThat(savedOrder)
                .isPresent()
                .contains(order);
    }

    @Test
    void shouldUpdateExistingOrder() {
        Order order = OrderTestDataBuilder.anOrder().status(OrderStatus.PLACED).build();
        orders.add(order);

        order = orders.ofId(order.id()).orElseThrow();
        order.markAsPaid();

        orders.add(order);

        order = orders.ofId(order.id()).orElseThrow();

        Assertions.assertThat(order.status()).isEqualTo(OrderStatus.PAID);
    }
}