package com.henio.algashop.ordering.domain.model.entity;

import com.henio.algashop.ordering.domain.model.valueobject.Product;
import com.henio.algashop.ordering.domain.model.valueobject.Quantity;
import com.henio.algashop.ordering.domain.model.valueobject.id.OrderId;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class OrderItemTest {

    @Test
    public void shouldGenerateOrderItem(){
        Product newProduct = ProductTestDataBuilder.aProduct().build();
        Quantity newQuantity = new Quantity(1);
        OrderId newOrderId = new OrderId();

        OrderItem orderItem = OrderItem.brandNew()
                .orderId(newOrderId)
                .product(newProduct)
                .quantity(newQuantity)
                .build();

        Assertions.assertWith(orderItem,
                o-> Assertions.assertThat(o.id()).isNotNull(),
                o-> Assertions.assertThat(o.productId()).isEqualTo(newProduct),
                o-> Assertions.assertThat(o.productName()).isEqualTo(newProduct.name()),
                o-> Assertions.assertThat(o.price()).isEqualTo(newProduct.price()),
                o-> Assertions.assertThat(o.quantity()).isEqualTo(newQuantity),
                o-> Assertions.assertThat(o.orderId()).isEqualTo(newOrderId)
        );
    }
}