package com.henio.algashop.ordering.domain.entity;

import com.henio.algashop.ordering.domain.exception.*;
import com.henio.algashop.ordering.domain.valueobject.*;
import com.henio.algashop.ordering.domain.valueobject.id.CustomerId;
import com.henio.algashop.ordering.domain.valueobject.id.OrderId;
import com.henio.algashop.ordering.domain.valueobject.id.OrderItemId;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class Order {

    private final OrderId id;
    private final CustomerId customerId;

    private Money totalAmount;
    private Quantity totalItems;

    private OffsetDateTime placedAt;
    private OffsetDateTime paidAt;
    private OffsetDateTime canceledAt;
    private OffsetDateTime readyAt;

    private Billing billing;
    private Shipping shipping;

    private OrderStatus status;
    private PaymentMethod paymentMethod;

    private final Set<OrderItem> items;

    private Order(CustomerId customerId) {
        this.id = OrderId.generate();

        this.customerId = Objects.requireNonNull(
                customerId,
                "Customer id is required"
        );

        this.totalAmount = Money.ZERO;
        this.totalItems = Quantity.ZERO;
        this.status = OrderStatus.DRAFT;
        this.items = new HashSet<>();
    }

    public static Order draft(CustomerId customerId) {
        return new Order(customerId);
    }

    public void addItem(Product product, Quantity quantity) {
        Objects.requireNonNull(product, "Product cannot be null");
        Objects.requireNonNull(quantity, "Quantity cannot be null");

        verifyIfChangeable();

        product.checkOutOfStock();

        OrderItem item = OrderItem.create(this.id, product, quantity);

        items.add(item);
        recalculateTotal();
    }

    public void place() {
        this.verifyIfCanChangeToPlaced();
        placedAt = OffsetDateTime.now();
        status = changeStatus(OrderStatus.PLACED);
    }

    public void markAsPaid() {
        this.paidAt = OffsetDateTime.now();
        status = changeStatus(OrderStatus.PAID);
    }

    public void changePaymentMethod(PaymentMethod paymentMethod) {
        Objects.requireNonNull(paymentMethod, "Payment method cannot be null");
        verifyIfChangeable();
        this.paymentMethod = paymentMethod;
    }

    public void changeBilling(Billing billing) {
        Objects.requireNonNull(billing, "Billing info cannot be null");
        verifyIfChangeable();
        this.billing = billing;
    }

    public void changeShipping(Shipping shipping) {
        Objects.requireNonNull(shipping, "Shipping info cannot be null");

        if(shipping.expectedDate().isBefore(LocalDate.now())) {
            throw new OrderInvalidShippingDeliveryDateException(id);
        }

        verifyIfChangeable();

        this.shipping = shipping;
    }

    public void changeItemQuantity(OrderItemId orderItemId, Quantity quantity) {
        Objects.requireNonNull(orderItemId, "Order item id cannot be null");
        Objects.requireNonNull(quantity, "Quantity cannot be null");

        verifyIfChangeable();

        OrderItem orderItem = findOrderItem(orderItemId);
        orderItem.changeQuantity(quantity);
        recalculateTotal();
    }

    private void recalculateTotal() {
        BigDecimal totalItemsAmount = items.stream().map(i -> i.totalAmount().value()).reduce(BigDecimal.ZERO, BigDecimal::add);

        Integer totalItemsQuantity = items.stream().map(i -> i.quantity().value()).reduce(0, Integer::sum);

        BigDecimal shippingCost;
        if(this.shipping == null) {
            shippingCost = BigDecimal.ZERO;
        } else {
            shippingCost = this.shipping.cost().value();
        }

        BigDecimal totalAmountWithShippingCost = totalItemsAmount.add(shippingCost);

        this.totalAmount = new Money(totalAmountWithShippingCost);
        this.totalItems = new Quantity(totalItemsQuantity);


    }

    private OrderStatus changeStatus(OrderStatus newOrderStatus) {
        Objects.requireNonNull(newOrderStatus, "Order status cannot be null");
        if(this.status.cannotChangeTo(newOrderStatus)) {
            throw new OrderStatusCannotBeChangedException(this.id, status, newOrderStatus);
        }
        return this.status = newOrderStatus;
    }

    private void verifyIfCanChangeToPlaced() {
        if (this.shipping() == null) {
            throw OrderCannotBePlacedException.noShippingInfo(this.id());
        }
        if (this.billing() == null) {
            throw OrderCannotBePlacedException.noBillingInfo(this.id());
        }
        if (this.paymentMethod() == null) {
            throw OrderCannotBePlacedException.noPaymentMethod(this.id());
        }
        if (this.items() == null || this.items().isEmpty()) {
            throw OrderCannotBePlacedException.noItems(this.id());
        }
    }

    private void verifyIfChangeable() {
        if(!isDraft()) {
            throw new OrderCannotBeEditedException(this.id, this.status);
        }
    }

    private OrderItem findOrderItem(OrderItemId orderItemId) {
        Objects.requireNonNull(orderItemId, "Order item id cannot be null");
        return this.items.stream()
                .filter(i -> i.id().equals(orderItemId))
                .findFirst()
                .orElseThrow(()-> new OrderDoesNotContainOrderItemException(this.id, orderItemId));
    }

    public boolean isDraft() {
        return OrderStatus.DRAFT.equals(status);
    }

    public boolean isPlaced() {
        return OrderStatus.PLACED.equals(status);
    }

    public boolean isPaid() {
        return OrderStatus.PAID.equals(status);
    }

    public boolean isReady() {
        return OrderStatus.READY.equals(status);
    }

    public boolean isCanceled() {
        return OrderStatus.CANCELED.equals(status);
    }

    public OrderId id() {
        return id;
    }

    public CustomerId customerId() {
        return customerId;
    }

    public Money totalAmount() {
        return totalAmount;
    }

    public Quantity totalItems() {
        return totalItems;
    }

    public OffsetDateTime placedAt() {
        return placedAt;
    }

    public OffsetDateTime paidAt() {
        return paidAt;
    }

    public OffsetDateTime canceledAt() {
        return canceledAt;
    }

    public OffsetDateTime readyAt() {
        return readyAt;
    }

    public Billing billing() {
        return billing;
    }

    public Shipping shipping() {
        return shipping;
    }

    public OrderStatus status() {
        return status;
    }

    public PaymentMethod paymentMethod() {
        return paymentMethod;
    }

    public Set<OrderItem> items() {
        return Collections.unmodifiableSet(items);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return Objects.equals(id, order.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
