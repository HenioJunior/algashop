package com.henio.algashop.ordering.domain.entity;

import com.henio.algashop.ordering.domain.exception.ShoppingCartDoesNotContainItemException;
import com.henio.algashop.ordering.domain.exception.ShoppingCartDoesNotContainProductException;
import com.henio.algashop.ordering.domain.valueobject.Product;
import com.henio.algashop.ordering.domain.valueobject.Quantity;
import com.henio.algashop.ordering.domain.valueobject.id.ProductId;
import com.henio.algashop.ordering.domain.valueobject.id.ShoppingCartId;
import com.henio.algashop.ordering.domain.valueobject.Money;
import com.henio.algashop.ordering.domain.valueobject.id.CustomerId;
import com.henio.algashop.ordering.domain.valueobject.id.ShoppingCartItemId;

import java.time.OffsetDateTime;
import java.util.*;

public class ShoppingCart {
    private ShoppingCartId id;
    private CustomerId customerId;
    private Money totalAmount;
    private Quantity totalItems;
    private OffsetDateTime createdAt;
    private Set<ShoppingCartItem> items;

    private ShoppingCart(CustomerId customerId) {
        this.id = ShoppingCartId.generate();
        this.customerId = Objects.requireNonNull(customerId, "Customer id is required");
        this.totalAmount = Money.ZERO;
        this.totalItems = Quantity.ZERO;
        this.createdAt = OffsetDateTime.now();
        this.items = new HashSet<>();
    }

    public static ShoppingCart create(CustomerId customerId) {
        return new ShoppingCart(customerId);
    }

    public void empty() {
        items.clear();
        totalAmount = Money.ZERO;
        totalItems = Quantity.ZERO;
    }

    public void removeItem(ShoppingCartItemId shoppingCartItemId) {
        ShoppingCartItem shoppingCartItem = this.requireItem(shoppingCartItemId);
        this.items.remove(shoppingCartItem);
        this.recalculateTotal();
    }

    public void addItem(Product product, Quantity quantity) {
        Objects.requireNonNull(product, "Product cannot be null");
        Objects.requireNonNull(quantity, "Quantity cannot be null");

        product.checkOutOfStock();

        findItemByProduct(product.id())
                .ifPresentOrElse(
                        item -> updateItem(item, product, quantity),
                        () -> insertItem(product, quantity));

        this.recalculateTotal();
    }

    public ShoppingCartItem requireItem(ShoppingCartItemId shoppingCartItemId) {
        Objects.requireNonNull(shoppingCartItemId, "Shopping cart item id cannot be null");
        return this.items.stream()
                .filter(item -> item.id().equals(shoppingCartItemId))
                .findFirst()
                .orElseThrow(() -> new ShoppingCartDoesNotContainItemException(this.id(), shoppingCartItemId));
    }

    public ShoppingCartItem requireItemByProduct(ProductId productId) {
        Objects.requireNonNull(productId, "Product id cannot be null");
        return this.items.stream()
                .filter(item -> item.productId().equals(productId))
                .findFirst()
                .orElseThrow(() -> new ShoppingCartDoesNotContainProductException(this.id(), productId));
    }

    public void refreshItem(Product product) {
        ShoppingCartItem shoppingCartItem = this.requireItemByProduct(product.id());
        shoppingCartItem.refresh(product);
        this.recalculateTotal();
    }

    public void changeItemQuantity(ShoppingCartItemId shoppingCartItemId, Quantity quantity) {
        ShoppingCartItem shoppingCartItem = this.requireItem(shoppingCartItemId);
        shoppingCartItem.changeQuantity(quantity);
        this.recalculateTotal();
    }

    public boolean containsUnavailableItems() {
        return items.stream().anyMatch(i -> !i.isAvailable());
    }

    public boolean isEmpty() {
        return items().isEmpty();
    }

    public Set<ShoppingCartItem> items() {
        return Collections.unmodifiableSet(items);
    }

    private void updateItem(
            ShoppingCartItem item,
            Product product,
            Quantity quantity
    ) {
        item.refresh(product);
        item.changeQuantity(item.quantity().add(quantity));
    }

    private void insertItem(Product product, Quantity quantity) {
        ShoppingCartItem item = ShoppingCartItem.create(
                this.id,
                product.id(),
                product.name(),
                product.price(),
                quantity
        );
        this.items.add(item);
    }

    private Optional<ShoppingCartItem> findItemByProduct(ProductId productId) {
        Objects.requireNonNull(productId);
        return this.items.stream()
                .filter(i -> i.productId().equals(productId))
                .findFirst();
    }

    private void recalculateTotal() {
        this.totalAmount = items.stream()
                .map(ShoppingCartItem::totalAmount)
                .reduce(Money.ZERO, Money::add);

        this.totalItems = items.stream()
                .map(ShoppingCartItem::quantity)
                .reduce(Quantity.ZERO, Quantity::add);
    }

    public ShoppingCartId id() {
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

    public OffsetDateTime createdAt() {
        return createdAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ShoppingCart that = (ShoppingCart) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "ShoppingCart{" +
                "id=" + id +
                ", customerId=" + customerId +
                ", totalAmount=" + totalAmount +
                ", totalItems=" + totalItems +
                ", createdAt=" + createdAt +
                ", items=" + items +
                '}';
    }
}
