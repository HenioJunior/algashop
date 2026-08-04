package com.henio.algashop.ordering.domain.entity;

import com.henio.algashop.ordering.domain.valueobject.Money;
import com.henio.algashop.ordering.domain.valueobject.ProductName;
import com.henio.algashop.ordering.domain.valueobject.Quantity;
import com.henio.algashop.ordering.domain.valueobject.id.OrderId;
import com.henio.algashop.ordering.domain.valueobject.id.OrderItemId;
import com.henio.algashop.ordering.domain.valueobject.id.ProductId;

import java.util.Objects;

public class OrderItem {

    private OrderItemId id;
    private OrderId orderId;

    private ProductId productId;
    private ProductName productName;

    private Money price;
    private Quantity quantity;

    private Money totalAmount;

    private OrderItem(OrderId orderId, ProductId productId, ProductName productName, Money price, Quantity quantity) {
        this.id = OrderItemId.generate();
        this.orderId = Objects.requireNonNull(orderId, "Order id is required");
        this.productId = Objects.requireNonNull(productId, "Product id is required");
        this.productName = Objects.requireNonNull(productName, "Product name is required");
        this.price = Objects.requireNonNull(price, "Price is required");
        this.quantity = Objects.requireNonNull(quantity, "Quantity is required");

        this.totalAmount = price.multiply(quantity);
    }

    public static OrderItem create(
            OrderId orderId,
            ProductId productId,
            ProductName productName,
            Money price,
            Quantity quantity) {
        return new OrderItem(orderId, productId, productName, price, quantity);
    }

    public OrderItemId id() {
        return id;
    }

    public OrderId orderId() {
        return orderId;
    }

    public ProductId productId() {
        return productId;
    }

    public ProductName productName() {
        return productName;
    }

    public Money price() {
        return price;
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
