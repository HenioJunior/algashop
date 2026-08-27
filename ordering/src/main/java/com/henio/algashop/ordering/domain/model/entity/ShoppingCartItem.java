package com.henio.algashop.ordering.domain.model.entity;

import com.henio.algashop.ordering.domain.model.exception.ShoppingCartItemIncompatibleProductException;
import com.henio.algashop.ordering.domain.model.valueobject.Money;
import com.henio.algashop.ordering.domain.model.valueobject.Product;
import com.henio.algashop.ordering.domain.model.valueobject.ProductName;
import com.henio.algashop.ordering.domain.model.valueobject.Quantity;
import com.henio.algashop.ordering.domain.model.valueobject.id.ProductId;
import com.henio.algashop.ordering.domain.model.valueobject.id.ShoppingCartId;
import com.henio.algashop.ordering.domain.model.valueobject.id.ShoppingCartItemId;
import lombok.Builder;

import java.util.Objects;

public class ShoppingCartItem {

    private final ShoppingCartItemId id;
    private final ShoppingCartId shoppingCartId;
    private final ProductId productId;
    private ProductName productName;
    private Money price;
    private Quantity quantity;
    private boolean available;
    private Money totalAmount;

    @Builder(builderClassName = "ExistingShoppingCartItem", builderMethodName = "existing")
    public ShoppingCartItem(
            ShoppingCartItemId id,
            ShoppingCartId shoppingCartId,
            ProductId productId,
            ProductName productName,
            Money price,
            Quantity quantity,
            Boolean available,
            Money totalAmount)
    {
        this.id = id;
        this.shoppingCartId = shoppingCartId;
        this.productId = productId;
        this.productName = productName;
        this.price = price;
        this.quantity = quantity;
        this.available = available;
        this.totalAmount = totalAmount;
    }

    @Builder(builderClassName = "BrandNewShoppingCartItem", builderMethodName = "brandNew")
    public ShoppingCartItem(ShoppingCartId shoppingCartId,
                            ProductId productId, ProductName productName, Money price,
                            Quantity quantity, Boolean available) {
        this(new ShoppingCartItemId(), shoppingCartId, productId, productName, price, quantity, available, Money.ZERO);
        this.recalculateTotal();
    }

    void refresh(Product product) {
        Objects.requireNonNull(product);
        Objects.requireNonNull(product.id());

        if (!product.id().equals(this.productId())) {
            throw new ShoppingCartItemIncompatibleProductException(this.id(), this.productId());
        }

        this.price = product.price();
        this.available = product.inStock();
        this.productName = product.name();
        this.recalculateTotal();
    }

    void changeQuantity(Quantity quantity) {
        Objects.requireNonNull(quantity);
        this.quantity = quantity;
        this.recalculateTotal();
    }

    private void recalculateTotal() {
        totalAmount = (price.multiply(quantity));
    }

    public ShoppingCartItemId id() {
        return id;
    }

    public ShoppingCartId shoppingCartId() {
        return shoppingCartId;
    }

    public ProductId productId() {
        return productId;
    }

    public ProductName name() {
        return productName;
    }

    public Money price() {
        return price;
    }

    public Quantity quantity() {
        return quantity;
    }

    public Boolean isAvailable() {
        return available;
    }

    public Money totalAmount() {
        return totalAmount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ShoppingCartItem that = (ShoppingCartItem) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "ShoppingCartItem{" +
                "id=" + id +
                ", shoppingCartId=" + shoppingCartId +
                ", productId=" + productId +
                ", productName=" + productName +
                ", price=" + price +
                ", quantity=" + quantity +
                ", available=" + available +
                ", totalAmount=" + totalAmount +
                '}';
    }
}
