package com.henio.algashop.ordering.domain.model.entity;

import com.henio.algashop.ordering.domain.model.valueobject.Money;
import com.henio.algashop.ordering.domain.model.valueobject.Product;
import com.henio.algashop.ordering.domain.model.valueobject.ProductName;
import com.henio.algashop.ordering.domain.model.valueobject.Quantity;
import com.henio.algashop.ordering.domain.model.valueobject.id.OrderId;
import com.henio.algashop.ordering.domain.model.valueobject.id.OrderItemId;
import com.henio.algashop.ordering.domain.model.valueobject.id.ProductId;
import lombok.Builder;
import lombok.Data;
import lombok.Setter;

import java.util.Objects;

@Setter
public class OrderItem {

    private OrderItemId id;
    private OrderId orderId;
    private ProductId productId;
    private ProductName productName;

    private Money price;
    private Quantity quantity;

    private Money totalAmount;

    @Builder(builderClassName = "ExistingOrderItemBuilder", builderMethodName = "existing")
    public OrderItem(OrderItemId id, OrderId orderId,
                     ProductId productId, ProductName productName,
                     Money price, Quantity quantity,
                     Money totalAmount) {
        this.setId(id);
        this.setOrderId(orderId);
        this.setProductId(productId);
        this.setProductName(productName);
        this.setPrice(price);
        this.setQuantity(quantity);
        this.setTotalAmount(totalAmount);
    }

    @Builder(builderClassName = "BrandNewOrderItemBuilder", builderMethodName = "brandNew")
    private static OrderItem createBrandNew(OrderId orderId,
                                            Product product,
                                            Quantity quantity) {
        Objects.requireNonNull(product);
        Objects.requireNonNull(orderId);
        Objects.requireNonNull(quantity);

        OrderItem orderItem = new OrderItem(
                new OrderItemId(),
                orderId,
                product.id(),
                product.name(),
                product.price(),
                quantity,
                Money.ZERO
        );

        orderItem.recalculateTotals();

        return orderItem;
    }

    void changeQuantity(Quantity quantity) {
        this.quantity = Objects.requireNonNull(quantity, "Quantity cannot be null");
        recalculateTotals();
    }

    private void recalculateTotals() {
        this.setTotalAmount(this.price().multiply(this.quantity()));
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

    private void setId(OrderItemId id) {
        Objects.requireNonNull(id, "OrderItemId cannot be null");
        this.id = id;
    }

    private void setOrderId(OrderId orderId) {
        Objects.requireNonNull(orderId, "OrderId cannot be null");
        this.orderId = orderId;
    }

    private void setProductId(ProductId productId) {
        Objects.requireNonNull(productId, "ProductId cannot be null");
        this.productId = productId;
    }

    private void setProductName(ProductName productName) {
        Objects.requireNonNull(productName, "ProductName cannot be null");
        this.productName = productName;
    }

    private void setPrice(Money price) {
        Objects.requireNonNull(price, "Price cannot be null");
        this.price = price;
    }

    private void setQuantity(Quantity quantity) {
        Objects.requireNonNull(quantity, "Quantity cannot be null");
        this.quantity = quantity;
    }

    private void setTotalAmount(Money totalAmount) {
        Objects.requireNonNull(totalAmount, "Total amount cannot be null");
        this.totalAmount = totalAmount;
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
