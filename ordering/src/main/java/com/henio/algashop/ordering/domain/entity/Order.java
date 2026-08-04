package com.henio.algashop.ordering.domain.entity;

import com.henio.algashop.ordering.domain.exception.OrderStatusCannotBeChangedException;
import com.henio.algashop.ordering.domain.valueobject.*;
import com.henio.algashop.ordering.domain.valueobject.id.CustomerId;
import com.henio.algashop.ordering.domain.valueobject.id.OrderId;
import com.henio.algashop.ordering.domain.valueobject.id.ProductId;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class Order {

    private OrderId id;
    private CustomerId customerId;

    private Money totalItemsAmount;
    private Quantity totalItemsQuantity;

    private OffsetDateTime placedAt;
    private OffsetDateTime paidAt;
    private OffsetDateTime canceledAt;
    private OffsetDateTime readyAt;

    private BillingInfo billing;
    private ShippingInfo shipping;

    private OrderStatus status;
    private PaymentMethod paymentMethod;

    private Money shippingCost;
    private LocalDate expectedDeliveryDate;

    private Set<OrderItem> items;

    private Order(CustomerId customerId) {
        this.id = OrderId.generate();

        this.customerId = Objects.requireNonNull(
                customerId,
                "Customer id is required"
        );

        this.totalItemsAmount = Money.ZERO;
        this.totalItemsQuantity = Quantity.ZERO;
        this.shippingCost = Money.ZERO;
        this.status = OrderStatus.DRAFT;
        this.items = new HashSet<>();
    }

    public static Order draft(CustomerId customerId) {
        return new Order(customerId);
    }

    public void addItem(
            ProductId productId,
            ProductName productName,
            Money price,
            Quantity quantity
    ) {

        OrderItem item = OrderItem.create(
                this.id,
                productId,
                productName,
                price,
                quantity
        );

        items.add(item);
        recalculateTotals();
    }

    public void place() {
        //TODO Business rules!
        placedAt = OffsetDateTime.now();
        status = changeStatus(OrderStatus.PLACED);
    }

    private OrderStatus changeStatus(OrderStatus newOrderStatus) {
    Objects.requireNonNull(newOrderStatus, "Order status cannot be null");
    if(this.status.cannotChangeTo(newOrderStatus)) {
        throw new OrderStatusCannotBeChangedException(this.id, status, newOrderStatus);
    }
    return this.status = newOrderStatus;
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
        return totalItemsAmount;
    }

    public Quantity totalItems() {
        return totalItemsQuantity;
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

    public BillingInfo billing() {
        return billing;
    }

    public ShippingInfo shipping() {
        return shipping;
    }

    public OrderStatus status() {
        return status;
    }

    public PaymentMethod paymentMethod() {
        return paymentMethod;
    }

    public Money shippingCost() {
        return shippingCost;
    }

    public LocalDate expectedDeliveryDate() {
        return expectedDeliveryDate;
    }

    public Set<OrderItem> items() {
        return Collections.unmodifiableSet(items);
    }

    public void recalculateTotals() {
        totalItemsAmount = items.stream()
                .map(OrderItem::totalAmount)
                .reduce(Money.ZERO, Money::add);

        totalItemsQuantity = items.stream()
                .map(OrderItem::quantity)
                .reduce(Quantity.ZERO, Quantity::add);

        if(this.shipping == null) {
            shippingCost = Money.ZERO;
        }

        totalItemsAmount = totalItemsAmount.add(shippingCost);
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
