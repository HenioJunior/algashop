package com.henio.algashop.ordering.domain.model.entity;

import com.henio.algashop.ordering.domain.model.valueobject.Product;
import com.henio.algashop.ordering.domain.model.valueobject.Quantity;
import com.henio.algashop.ordering.domain.model.valueobject.id.OrderId;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class OrderItemTest {

    @Test
    public void shouldGenerateOrderItem(){
        Product product = ProductTestDataBuilder.aProduct().build();
        Quantity quantity = new Quantity(1);
        OrderId orderId = new OrderId();

        OrderItem orderItem = OrderItem.create(orderId, product, quantity);

        Assertions.assertWith(orderItem,
                o-> Assertions.assertThat(o.id()).isNotNull(),
                o-> Assertions.assertThat(o.product().id()).isEqualTo(product.id()),
                o-> Assertions.assertThat(o.product().name()).isEqualTo(product.name()),
                o-> Assertions.assertThat(o.product().price()).isEqualTo(product.price()),
                o-> Assertions.assertThat(o.quantity()).isEqualTo(quantity),
                o-> Assertions.assertThat(o.orderId()).isEqualTo(orderId)
        );
    }
}