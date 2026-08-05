package com.henio.algashop.ordering.domain.entity;

import com.henio.algashop.ordering.domain.valueobject.Money;
import com.henio.algashop.ordering.domain.valueobject.Product;
import com.henio.algashop.ordering.domain.valueobject.ProductName;
import com.henio.algashop.ordering.domain.valueobject.Quantity;
import com.henio.algashop.ordering.domain.valueobject.id.OrderId;
import com.henio.algashop.ordering.domain.valueobject.id.ProductId;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

class OrderItemTest {

    @Test
    public void shouldGenerateOrderItem(){
        Product product = Product.builder()
                .id(ProductId.generate())
                .name(new ProductName("Apple Watch"))
                .price(new Money(new BigDecimal(5000)))
                .inStock(true)
                .build();
        OrderItem.create(OrderId.generate(), product, new Quantity(1));
    }
}