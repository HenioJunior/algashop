package com.henio.algashop.ordering.domain.model.entity;

import com.henio.algashop.ordering.domain.model.valueobject.Money;
import com.henio.algashop.ordering.domain.model.valueobject.Product;
import com.henio.algashop.ordering.domain.model.valueobject.Quantity;
import com.henio.algashop.ordering.domain.model.valueobject.id.OrderId;
import com.henio.algashop.ordering.domain.model.valueobject.id.OrderItemId;

import java.util.Objects;

public class OrderItem {

    private OrderItemId id;
    private OrderId orderId;
    private Product product;
    private Quantity quantity;

    private Money totalAmount;

    private OrderItem(OrderId orderId, Product product, Quantity quantity) {
        this.id = OrderItemId.generate();
        this.orderId = Objects.requireNonNull(orderId, "Order id is required");
        this.product = Objects.requireNonNull(product, "Product is required");
        this.quantity = Objects.requireNonNull(quantity, "Quantity is required");
        recalculateTotal();

    }

    public static OrderItem create(
            OrderId orderId,
            Product product,
            Quantity quantity) {

        return new OrderItem(orderId, product, quantity);
    }

    void changeQuantity(Quantity quantity) {
        this.quantity = Objects.requireNonNull(quantity, "Quantity cannot be null");
        recalculateTotal();
    }

    private void recalculateTotal() {
        this.totalAmount = product.price().multiply(quantity);
    }

    public OrderItemId id() {
        return id;
    }

    public OrderId orderId() {
        return orderId;
    }

    public Product product() {
        return product;
    }

    public Quantity quantity() {
        return quantity;
    }

    public Money totalAmount() {
        return totalAmount;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        OrderItem orderItem = (OrderItem) o;
        return Objects.equals(id, orderItem.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

}
