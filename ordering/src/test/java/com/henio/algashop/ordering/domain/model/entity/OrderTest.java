package com.henio.algashop.ordering.domain.model.entity;

import com.henio.algashop.ordering.domain.model.exception.OrderCannotBeEditedException;
import com.henio.algashop.ordering.domain.model.exception.OrderInvalidShippingDeliveryDateException;
import com.henio.algashop.ordering.domain.model.exception.OrderStatusCannotBeChangedException;
import com.henio.algashop.ordering.domain.model.exception.ProductOutOfStockException;
import com.henio.algashop.ordering.domain.model.valueobject.*;
import com.henio.algashop.ordering.domain.model.valueobject.id.CustomerId;
import com.henio.algashop.ordering.domain.model.valueobject.id.ProductId;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;

class OrderTest {

    @Test
    public void shouldGenerateDraftOrder() {
        CustomerId customerId = CustomerId.generate();
        Order order = Order.draft(customerId);
        Assertions.assertWith(order,
                o -> Assertions.assertThat(o.id()).isNotNull(),
                o -> Assertions.assertThat(o.customerId()).isEqualTo(customerId),
                o -> Assertions.assertThat(o.totalAmount()).isEqualTo(Money.ZERO),
                o -> Assertions.assertThat(o.totalItems()).isEqualTo(Quantity.ZERO),
                o -> Assertions.assertThat(o.isDraft()).isTrue(),
                o -> Assertions.assertThat(o.items()).isEmpty(),

                o -> Assertions.assertThat(o.placedAt()).isNull(),
                o -> Assertions.assertThat(o.paidAt()).isNull(),
                o -> Assertions.assertThat(o.canceledAt()).isNull(),
                o -> Assertions.assertThat(o.readyAt()).isNull(),
                o -> Assertions.assertThat(o.billing()).isNull(),
                o -> Assertions.assertThat(o.shipping()).isNull(),
                o -> Assertions.assertThat(o.paymentMethod()).isNull()

        );
    }

    @Test
    public void shouldAddItem() {
        Order order = Order.draft(new CustomerId());
        Product product = ProductTestDataBuilder.aProductAltMousePad().build();
        ProductId productId = product.id();

        order.addItem(product, new Quantity(1));

        Assertions.assertThat(order.items().size()).isEqualTo(1);

        OrderItem orderItem = order.items().iterator().next();

        Assertions.assertWith(orderItem,
                (i) -> Assertions.assertThat(i.id()).isNotNull(),
                (i) -> Assertions.assertThat(i.productName()).isEqualTo(new ProductName("Mouse Pad")),
                (i) -> Assertions.assertThat(i.productId()).isEqualTo(productId),
                (i) -> Assertions.assertThat(i.price()).isEqualTo(new Money("100")),
                (i) -> Assertions.assertThat(i.quantity()).isEqualTo(new Quantity(1))
        );
    }

    @Test
    public void shouldGenerateExceptionWhenTryToChangeItemSet() {
        Order order = Order.draft(new CustomerId());
        Product product = ProductTestDataBuilder.aProductAltMousePad().build();

        order.addItem(product, new Quantity(1));

        Set<OrderItem> items = order.items();

        Assertions.assertThatExceptionOfType(UnsupportedOperationException.class)
                .isThrownBy(items::clear);
    }

    @Test
    public void shouldCalculateTotals() {
        Order order = Order.draft(new CustomerId());

        order.addItem(
                ProductTestDataBuilder.aProductAltMousePad().build(),
                new Quantity(2)
        );

        order.addItem(
                ProductTestDataBuilder.aProductAltRamMemory().build(),
                new Quantity(1)
        );

        Assertions.assertThat(order.totalAmount()).isEqualTo(new Money("400"));
        Assertions.assertThat(order.totalItems()).isEqualTo(new Quantity(3));
    }

    @Test
    void givenDraftOrder_whenAddItem_thenShouldAddItemToOrder() {
        Product product = ProductTestDataBuilder.aProductAltMousePad().build();

        Quantity quantity = new Quantity(2);
        Order order = Order.draft(CustomerId.generate());

        order.addItem(product, quantity);

        assertThat(order.items())
                .singleElement()
                .satisfies(item -> {
                    assertThat(item.orderId())
                            .isEqualTo(order.id());

                    assertThat(item.productId())
                            .isEqualTo(product.id());

                    assertThat(item.productName())
                            .isEqualTo(product.name());

                    assertThat(item.price())
                            .isEqualTo(product.price());

                    assertThat(item.quantity())
                            .isEqualTo(quantity);

                    assertThat(item.totalAmount())
                            .isEqualTo(new Money("200.00"));
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
    void givenDraftOrder_whenChangeBilling_thenShouldAllowChange() {
        Order order = OrderTestDataBuilder.anOrder().build();

        Billing billing = OrderTestDataBuilder.aBilling();

        order.changeBilling(billing);

        assertThat(order.billing()).isEqualTo(billing);
    }

    @Test
    void givenDraftOrder_whenChangeShipping_thenShouldAllowChange() {
        Shipping shipping = OrderTestDataBuilder.aShipping();

        Order order = Order.draft(CustomerId.generate());

        order.changeShipping(
                Shipping.builder()
                        .address(OrderTestDataBuilder.anAddress())
                        .recipient(
                                Recipient.builder()
                                        .fullName(new FullName("John", "Doe"))
                                        .document(new Document("112-33-2321"))
                                        .phone(new Phone("111-441-1244"))
                                        .build())
                        .cost(new Money("10.00"))
                        .expectedDate(
                                LocalDate.now()
                                        .plusDays(1))
                        .build()
        );

        Assertions.assertWith(
                order,
                o -> assertThat(o.shipping())
                        .isEqualTo(shipping),
                o -> assertThat(o.shipping().cost())
                        .isEqualTo(shipping.cost()),
                o -> assertThat(o.shipping().expectedDate())
                        .isEqualTo(shipping.expectedDate())
        );
    }

    @Test
    void givenDraftOrderAndPastDeliveryDate_whenChangeShipping_thenShouldThrowException() {
        Shipping aPastDateShipping = OrderTestDataBuilder.aPastDateShipping();

        Order order = Order.draft(CustomerId.generate());

        assertThatExceptionOfType(OrderInvalidShippingDeliveryDateException.class)
                .isThrownBy(() -> order.changeShipping(aPastDateShipping));
    }

    @Test
    void givenDraftOrder_whenChangeItemQuantity_thenShouldRecalculateTotals() {
        Order order = Order.draft(CustomerId.generate());

        Product product = ProductTestDataBuilder.aProduct().build();

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
                        .isEqualTo(new Money("15000.00")),
                o -> assertThat(o.totalItems())
                        .isEqualTo(new Quantity(5)),
                o -> assertThat(o.items())
                        .singleElement()
                        .extracting(OrderItem::quantity)
                        .isEqualTo(new Quantity(5))
        );
    }

    @Test
    public void givenOutOfStockProduct_whenTryToAddToAnOrder_shouldNotAllow() {
        Order order = Order.draft(new CustomerId());

        ThrowableAssert.ThrowingCallable addItemTask = () -> order.addItem(
                ProductTestDataBuilder.aProductUnavailable().build(),
                new Quantity(1)
        );

        Assertions.assertThatExceptionOfType(ProductOutOfStockException.class).isThrownBy(addItemTask);
    }

    @Test
    public void givenPlacedOrder_whenTryToEdit_shouldThrowOrderCannotBeEditedException() {
        Order order = Order.draft(new CustomerId());

        Product product = ProductTestDataBuilder.aProduct().build();
        Quantity initialQuantity = new Quantity(1);
        Shipping shipping = OrderTestDataBuilder.aShipping();
        Billing billing = OrderTestDataBuilder.aBilling();
        PaymentMethod paymentMethod = PaymentMethod.CREDIT_CARD;

        order.addItem(product, initialQuantity);
        order.changeShipping(shipping);
        order.changePaymentMethod(paymentMethod);
        order.changeBilling(billing);

        order.place();

        List<ThrowableAssert.ThrowingCallable> operations = List.of(
                () -> order.changeShipping(shipping),
                () -> order.changePaymentMethod(paymentMethod),
                () -> order.changeBilling(billing),
                () -> order.addItem(ProductTestDataBuilder.aProduct().build(), new Quantity(1))
        );

        operations.forEach(operation ->
                assertThatExceptionOfType(OrderCannotBeEditedException.class)
                        .isThrownBy(operation)
        );
    }

    @Test
    void givenDraftOrderWithTwoItems_whenRemoveItem_shouldUpdateItemsAndTotals() {
        Order order = Order.draft(new CustomerId());

        Product firstProduct = ProductTestDataBuilder.aProduct()
                .price(new Money("10.00"))
                .build();

        Product secondProduct = ProductTestDataBuilder.aProduct()
                .price(new Money("20.00"))
                .build();

        order.addItem(firstProduct, new Quantity(2));
        order.addItem(secondProduct, new Quantity(3));

        OrderItem itemToRemove = order.items()
                .stream()
                .filter(item -> item.productId().equals(firstProduct.id()))
                .findFirst()
                .orElseThrow();

        order.removeItem(itemToRemove.id());

        assertThat(order.items())
                .singleElement()
                .extracting(OrderItem::productId)
                .isEqualTo(secondProduct.id());

        assertThat(order.totalAmount())
                .isEqualTo(new Money("60.00"));
    }

}