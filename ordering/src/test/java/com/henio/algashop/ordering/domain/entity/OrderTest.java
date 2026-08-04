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
        Order order = OrderTestDataBuilder.anOrder().build();
        order.place();
        Assertions.assertThat(order.isPlaced()).isTrue();
    }

    @Test
    public void givenPlacedOrder_whenTryToPlace_shouldGenerateException() {
        Order order = OrderTestDataBuilder.anOrder().status(OrderStatus.PLACED).build();
        Assertions.assertThatExceptionOfType(OrderStatusCannotBeChangedException.class)
                .isThrownBy(order::place);
    }

    @Test
    public void givenDraftOrder_whenChangePaymentMethod_shouldAllowChange() {
        Order order = Order.draft(new CustomerId());
        order.changePaymentMethod(PaymentMethod.CREDIT_CARD);
        Assertions.assertWith(order.paymentMethod()).isEqualTo(PaymentMethod.CREDIT_CARD);
    }

    @Test
    public void givenDraftOrder_whenChangeBillingInfo_shouldAllowChange() {
        Order order = OrderTestDataBuilder.anOrder().build();

        order.changeBilling(order.billing());

        BillingInfo expectedBillingInfo = BillingInfo.builder()
                .address(order.billing().address())
                .document(new Document("225-09-1992"))
                .phone(new Phone("123-111-9911"))
                .fullName(new FullName("John", "Doe"))
                .build();

        Assertions.assertThat(order.billing()).isEqualTo(expectedBillingInfo);
    }

    @Test
    public void givenDraftOrder_whenChangeShippingInfo_shouldAllowChange() {
        Address address = Address.builder()
                .street("Bourbon Street")
                .number("1234")
                .neighborhood("North Ville")
                .complement("apt. 11")
                .city("Montfort")
                .state("South Carolina")
                .zipCode(new ZipCode("79911")).build();

        ShippingInfo shippingInfo = ShippingInfo.builder()
                .address(address)
                .fullName(new FullName("John", "Doe"))
                .document(new Document("112-33-2321"))
                .phone(new Phone("111-441-1244"))
                .build();

        Order order = Order.draft(new CustomerId());
        Money shippingCost = Money.ZERO;
        LocalDate expectedDeliveryDate = LocalDate.now().plusDays(1);

        order.changeShipping(shippingInfo, shippingCost, expectedDeliveryDate);
    }

    @Test
    public void givenDraftOrderAndDeliveryDateInThePast_whenChangeShippingInfo_shouldNotAllowChange() {
        Address address = Address.builder()
                .street("Bourbon Street")
                .number("1234")
                .neighborhood("North Ville")
                .complement("apt. 11")
                .city("Montfort")
                .state("South Carolina")
                .zipCode(new ZipCode("79911")).build();

        ShippingInfo shippingInfo = ShippingInfo.builder()
                .address(address)
                .fullName(new FullName("John", "Doe"))
                .document(new Document("112-33-2321"))
                .phone(new Phone("111-441-1244"))
                .build();

        Order order = Order.draft(new CustomerId());
        Money shippingCost = Money.ZERO;

        LocalDate expectedDeliveryDate = LocalDate.now().minusDays(2);

        Assertions.assertThatExceptionOfType(OrderInvalidShippingDeliveryDateException.class)
                .isThrownBy(()-> order.changeShipping(shippingInfo, shippingCost, expectedDeliveryDate));
    }

    @Test
    public void givenDraftOrder_whenChangeItem_shouldRecalculate() {
        Order order = Order.draft(new CustomerId());

        order.addItem(
                new ProductId(),
                new ProductName("Destkop X11"),
                new Money("10.00"),
                new Quantity(3)
        );

        OrderItem orderItem = order.items().iterator().next();

        order.changeItemQuantity(orderItem.id(), new Quantity(5));

        Assertions.assertWith(order,
                (o) -> Assertions.assertThat(o.totalAmount()).isEqualTo(new Money("50.00")),
                (o) -> Assertions.assertThat(o.totalItems()).isEqualTo(new Quantity(5))
        );
    }
}