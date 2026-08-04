package com.henio.algashop.ordering.domain.entity;

import com.henio.algashop.ordering.domain.exception.OrderStatusCannotBeChangedException;
import com.henio.algashop.ordering.domain.valueobject.Money;
import com.henio.algashop.ordering.domain.valueobject.ProductName;
import com.henio.algashop.ordering.domain.valueobject.Quantity;
import com.henio.algashop.ordering.domain.valueobject.id.CustomerId;
import com.henio.algashop.ordering.domain.valueobject.id.ProductId;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;

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

    @Test
    void givenOrderWithItems_whenTryToModifyReturnedCollection_shouldThrowException() {
        Order order = Order.draft(CustomerId.generate());

        order.addItem(
                ProductId.generate(),
                new ProductName("Notebook"),
                new Money("2500.00"),
                new Quantity(1)
        );

        assertThatExceptionOfType(UnsupportedOperationException.class)
                .isThrownBy(() ->
                        order.items().clear()
                );

        assertThat(order.items()).hasSize(1);
    }

    @Test
    void shouldCalculateTotals() {

        Order order = Order.draft(CustomerId.generate());

        order.addItem(
                ProductId.generate(),
                new ProductName("Notebook"),
                new Money("2500.00"),
                new Quantity(1)
        );

        order.addItem(
                ProductId.generate(),
                new ProductName("Apple Watch"),
                new Money("5000.00"),
                new Quantity(1)
        );

        Assertions.assertThat(order.totalAmount()).isEqualTo(new Money("7500.00"));
        Assertions.assertThat(order.totalItems()).isEqualTo(new Quantity(2));
    }

    @Test
    public void givenDraftOrder_whenPlace_shouldChangeToPlaced() {
        Order order = Order.draft(new CustomerId());
        order.place();
        Assertions.assertThat(order.isPlaced()).isTrue();
    }

    @Test
    public void givenPlacedOrder_whenTryToPlace_shouldGenerateException() {
        Order order = Order.draft(new CustomerId());
        order.place();
        Assertions.assertThatExceptionOfType(OrderStatusCannotBeChangedException.class)
                .isThrownBy(order::place);
    }
}