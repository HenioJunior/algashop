package com.henio.algashop.ordering.domain.entity;

import com.henio.algashop.ordering.domain.exception.OrderInvalidShippingDeliveryDateException;
import com.henio.algashop.ordering.domain.exception.OrderStatusCannotBeChangedException;
import com.henio.algashop.ordering.domain.valueobject.*;
import com.henio.algashop.ordering.domain.valueobject.id.CustomerId;
import com.henio.algashop.ordering.domain.valueobject.id.ProductId;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;

class OrderTest {

    @Test
    public void shouldGenerateOrder(){
        Order.draft(CustomerId.generate());
    }

    @Test
    void givenDraftOrder_whenAddItem_thenShouldAddItemToOrder() {
        Product product = new Product(
                ProductId.generate(),
                new ProductName("Notebook"),
                new Money("2500.00"),
                true
        );

        Quantity quantity = new Quantity(2);
        Order order = Order.draft(CustomerId.generate());

        order.addItem(product, quantity);

        assertThat(order.items())
                .singleElement()
                .satisfies(item -> {
                    assertThat(item.orderId())
                            .isEqualTo(order.id());

                    assertThat(item.product())
                            .isEqualTo(product);

                    assertThat(item.product().id())
                            .isEqualTo(product.id());

                    assertThat(item.product().name())
                            .isEqualTo(product.name());

                    assertThat(item.product().price())
                            .isEqualTo(product.price());

                    assertThat(item.quantity())
                            .isEqualTo(quantity);

                    assertThat(item.totalAmount())
                            .isEqualTo(new Money("5000.00"));
                });
    }

    @Test
    void givenOrderWithItems_whenTryToModifyReturnedCollection_thenShouldThrowException() {
        Order order = Order.draft(CustomerId.generate());

        order.addItem(
                new Product(
                        ProductId.generate(),
                        new ProductName("Notebook"),
                        new Money("2500.00"),
                        true
                ),
                new Quantity(1)
        );

        assertThatExceptionOfType(UnsupportedOperationException.class)
                .isThrownBy(() -> order.items().clear());

        assertThat(order.items()).hasSize(1);
    }

    @Test
    void givenOrderWithItems_whenCalculateTotals_thenShouldSumAmountAndQuantity() {
        Order order = Order.draft(CustomerId.generate());

        order.addItem(
                new Product(
                        ProductId.generate(),
                        new ProductName("Notebook"),
                        new Money("2500.00"),
                        true
                ),
                new Quantity(1)
        );

        order.addItem(
                new Product(
                        ProductId.generate(),
                        new ProductName("Apple Watch"),
                        new Money("5000.00"),
                        true
                ),
                new Quantity(1)
        );

        Assertions.assertWith(
                order,
                o -> assertThat(o.totalAmount())
                        .isEqualTo(new Money("7500.00")),
                o -> assertThat(o.totalItems())
                        .isEqualTo(new Quantity(2))
        );
    }

    @Test
    void givenDraftOrder_whenPlace_thenShouldChangeStatusToPlaced() {
        Order order = OrderTestDataBuilder.anOrder()
                .status(OrderStatus.DRAFT)
                .build();

        order.place();

        Assertions.assertWith(
                order,
                o -> assertThat(o.status())
                        .isEqualTo(OrderStatus.PLACED),
                o -> assertThat(o.isPlaced()).isTrue(),
                o -> assertThat(o.placedAt()).isNotNull()
        );
    }

    @Test
    void givenPlacedOrder_whenTryToPlaceAgain_thenShouldThrowException() {
        Order order = OrderTestDataBuilder.anOrder()
                .status(OrderStatus.PLACED)
                .build();

        assertThatExceptionOfType(
                OrderStatusCannotBeChangedException.class
        ).isThrownBy(order::place);
    }

    @Test
    void givenDraftOrder_whenChangePaymentMethod_thenShouldAllowChange() {
        Order order = Order.draft(CustomerId.generate());

        order.changePaymentMethod(PaymentMethod.CREDIT_CARD);

        assertThat(order.paymentMethod())
                .isEqualTo(PaymentMethod.CREDIT_CARD);
    }

    @Test
    void givenDraftOrder_whenChangeBillingInfo_thenShouldAllowChange() {
        Order order = OrderTestDataBuilder.anOrder().build();

        BillingInfo newBillingInfo = BillingInfo.builder()
                .address(OrderTestDataBuilder.anAddress())
                .document(new Document("225-09-1992"))
                .phone(new Phone("123-111-9911"))
                .fullName(new FullName("John", "Doe"))
                .build();

        order.changeBilling(newBillingInfo);

        assertThat(order.billing())
                .isEqualTo(newBillingInfo);
    }

    @Test
    void givenDraftOrder_whenChangeShippingInfo_thenShouldAllowChange() {
        ShippingInfo shippingInfo =
                OrderTestDataBuilder.aShippingInfo();

        Order order = Order.draft(CustomerId.generate());

        Money shippingCost = new Money("15.00");
        LocalDate expectedDeliveryDate =
                LocalDate.now().plusDays(1);

        order.changeShipping(
                shippingInfo,
                shippingCost,
                expectedDeliveryDate
        );

        Assertions.assertWith(
                order,
                o -> assertThat(o.shipping())
                        .isEqualTo(shippingInfo),
                o -> assertThat(o.shippingCost())
                        .isEqualTo(shippingCost),
                o -> assertThat(o.expectedDeliveryDate())
                        .isEqualTo(expectedDeliveryDate)
        );
    }

    @Test
    void givenDraftOrderAndPastDeliveryDate_whenChangeShippingInfo_thenShouldThrowException() {
        ShippingInfo shippingInfo =
                OrderTestDataBuilder.aShippingInfo();

        Order order = Order.draft(CustomerId.generate());

        LocalDate expectedDeliveryDate =
                LocalDate.now().minusDays(2);

        assertThatExceptionOfType(
                OrderInvalidShippingDeliveryDateException.class
        ).isThrownBy(() ->
                order.changeShipping(
                        shippingInfo,
                        Money.ZERO,
                        expectedDeliveryDate
                )
        );
    }

    @Test
    void givenDraftOrder_whenChangeItemQuantity_thenShouldRecalculateTotals() {
        Order order = Order.draft(CustomerId.generate());

        Product product = new Product(
                ProductId.generate(),
                new ProductName("Desktop X11"),
                new Money("10.00"),
                true
        );

        order.addItem(
                product,
                new Quantity(3)
        );

        OrderItem orderItem = order.items()
                .iterator()
                .next();

        order.changeItemQuantity(
                orderItem.id(),
                new Quantity(5)
        );

        Assertions.assertWith(
                order,
                o -> assertThat(o.totalAmount())
                        .isEqualTo(new Money("50.00")),
                o -> assertThat(o.totalItems())
                        .isEqualTo(new Quantity(5)),
                o -> assertThat(o.items())
                        .singleElement()
                        .extracting(OrderItem::quantity)
                        .isEqualTo(new Quantity(5))
        );
    }
}