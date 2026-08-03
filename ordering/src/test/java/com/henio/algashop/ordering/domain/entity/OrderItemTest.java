package com.henio.algashop.ordering.domain.entity;

import com.henio.algashop.ordering.domain.valueobject.Money;
import com.henio.algashop.ordering.domain.valueobject.ProductName;
import com.henio.algashop.ordering.domain.valueobject.Quantity;
import com.henio.algashop.ordering.domain.valueobject.id.OrderId;
import com.henio.algashop.ordering.domain.valueobject.id.ProductId;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

class OrderItemTest {

    @Test
    public void shouldGenerateOrderItem(){
        OrderItem.create(
                OrderId.generate(),
                ProductId.generate(),
                new ProductName("Apple Watch"),
                new Money(new BigDecimal(5000)),
                new Quantity(1)
        );
    }

}