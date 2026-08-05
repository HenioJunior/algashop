package com.henio.algashop.ordering.domain.entity;

import com.henio.algashop.ordering.domain.exception.OrderCannotBePlacedException;
import com.henio.algashop.ordering.domain.exception.OrderDoesNotContainOrderItemException;
import com.henio.algashop.ordering.domain.exception.OrderInvalidShippingDeliveryDateException;
import com.henio.algashop.ordering.domain.exception.OrderStatusCannotBeChangedException;
import com.henio.algashop.ordering.domain.valueobject.*;
import com.henio.algashop.ordering.domain.valueobject.id.CustomerId;
import com.henio.algashop.ordering.domain.valueobject.id.OrderId;
import com.henio.algashop.ordering.domain.valueobject.id.OrderItemId;
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

    public void addItem(Product product, Quantity quantity) {
        Objects.requireNonNull(product, "Product cannot be null");
        Objects.requireNonNull(quantity, "Quantity cannot be null");

        product.checkOutOfStock();

        OrderItem item = OrderItem.create(this.id, product, quantity);

        items.add(item);
        recalculateTotals();
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
        this.paymentMethod = paymentMethod;
    }

    public void changeBilling(BillingInfo billing) {
        Objects.requireNonNull(billing, "Billing info cannot be null");
        this.billing = billing;
    }

    public void changeShipping(ShippingInfo shipping, Money shippingCost, LocalDate expectedDeliveryDate) {
        Objects.requireNonNull(shipping, "Shipping info cannot be null");
        Objects.requireNonNull(shippingCost, "Shipping cost cannot be null");
        Objects.requireNonNull(expectedDeliveryDate, "Expected delivery date cannot be null");

        if(expectedDeliveryDate.isBefore(LocalDate.now())) {
            throw new OrderInvalidShippingDeliveryDateException(id);
        }

        this.shipping = shipping;
        this.shippingCost = shippingCost;
        this.expectedDeliveryDate = expectedDeliveryDate;
    }

    public void changeItemQuantity(OrderItemId orderItemId, Quantity quantity) {
        Objects.requireNonNull(orderItemId, "Order item id cannot be null");
        Objects.requireNonNull(quantity, "Quantity cannot be null");

        OrderItem orderItem = findOrderItem(orderItemId);
        orderItem.changeQuantity(quantity);
        recalculateTotals();
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

    private void recalculateTotals() {
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
        if (this.shippingCost() == null) {
            throw OrderCannotBePlacedException.invalidShippingCost(this.id());
        }
        if (this.expectedDeliveryDate() == null) {
            throw OrderCannotBePlacedException.invalidExpectedDeliveryDate(this.id());
        }
        if (this.items() == null || this.items().isEmpty()) {
            throw OrderCannotBePlacedException.noItems(this.id());
        }
    }

    private OrderItem findOrderItem(OrderItemId orderItemId) {
        Objects.requireNonNull(orderItemId, "Order item id cannot be null");
        return this.items.stream()
                .filter(i -> i.id().equals(orderItemId))
                .findFirst()
                .orElseThrow(()-> new OrderDoesNotContainOrderItemException(this.id(), orderItemId));
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
