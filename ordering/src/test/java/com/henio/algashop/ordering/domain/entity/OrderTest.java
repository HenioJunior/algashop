package com.henio.algashop.ordering.domain.entity;

import com.henio.algashop.ordering.domain.valueobject.Money;
import com.henio.algashop.ordering.domain.valueobject.ProductName;
import com.henio.algashop.ordering.domain.valueobject.Quantity;
import com.henio.algashop.ordering.domain.valueobject.id.CustomerId;
import com.henio.algashop.ordering.domain.valueobject.id.ProductId;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class OrderTest {

    @Test
    public void shouldGenerateOrder(){
        Order.draft(CustomerId.generate());
    }

    @Test
    void givenDraftOrder_whenAddItem_thenShouldAddItemToOrder() {
        CustomerId customerId = CustomerId.generate();
        ProductId productId = ProductId.generate();
        ProductName productName = new ProductName("Notebook");
        Money price = new Money("2500.00");
        Quantity quantity = new Quantity(2);

        Order order = Order.draft(customerId);

        order.addItem(
                productId,
                productName,
                price,
                quantity
        );

        assertThat(order.items())
                .singleElement()
                .satisfies(item -> {
                    assertThat(item.orderId()).isEqualTo(order.id());
                    assertThat(item.productId()).isEqualTo(productId);
                    assertThat(item.productName()).isEqualTo(productName);
                    assertThat(item.price()).isEqualTo(price);
                    assertThat(item.quantity()).isEqualTo(quantity);
                    assertThat(item.totalAmount())
                            .isEqualTo(new Money("5000.00"));
                });
    }


}