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

import java.math.BigDecimal;
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

    public static ShoppingCart startShopping(CustomerId customerId) {
        return new ShoppingCart(customerId);
    }

    public void empty() {
        items.clear();
        totalAmount = Money.ZERO;
        totalItems = Quantity.ZERO;
    }

    public void removeItem(ShoppingCartItemId shoppingCartItemId) {
        ShoppingCartItem shoppingCartItem = this.findItem(shoppingCartItemId);
        this.items.remove(shoppingCartItem);
        this.recalculateTotals();
    }

    public void addItem(Product product, Quantity quantity) {
        Objects.requireNonNull(product);
        Objects.requireNonNull(quantity);

        product.checkOutOfStock();

        ShoppingCartItem shoppingCartItem = ShoppingCartItem.brandNew(
                this.id,
                product.id(),
                product.name(),
                product.price(),
                quantity
        );

        searchItemByProduct(product.id()).ifPresentOrElse(i -> updateItem(i, product, quantity), () -> insertItem(shoppingCartItem));

        this.recalculateTotals();
    }

    public ShoppingCartItem findItem(ShoppingCartItemId shoppingCartItemId) {
        Objects.requireNonNull(shoppingCartItemId);
        return this.items.stream()
                .filter(i -> i.id().equals(shoppingCartItemId))
                .findFirst()
                .orElseThrow(() -> new ShoppingCartDoesNotContainItemException(this.id(), shoppingCartItemId));
    }

    public ShoppingCartItem findItem(ProductId productId) {
        Objects.requireNonNull(productId);
        return this.items.stream()
                .filter(i -> i.productId().equals(productId))
                .findFirst()
                .orElseThrow(() -> new ShoppingCartDoesNotContainProductException(this.id(), productId));
    }

    public void refreshItem(Product product) {
        ShoppingCartItem shoppingCartItem = this.findItem(product.id());
        shoppingCartItem.refresh(product);
        this.recalculateTotals();
    }

    public void changeItemQuantity(ShoppingCartItemId shoppingCartItemId, Quantity quantity) {
        ShoppingCartItem shoppingCartItem = this.findItem(shoppingCartItemId);
        shoppingCartItem.changeQuantity(quantity);
        this.recalculateTotals();
    }

    public boolean containsUnavailableItems() {
        return items.stream().anyMatch(i -> !i.isAvailable());
    }

    public boolean isEmpty() {
        return this.items().isEmpty();
    }

    public Set<ShoppingCartItem> items() {
        return Collections.unmodifiableSet(items);
    }

    private void updateItem(ShoppingCartItem shoppingCartItem, Product product, Quantity quantity) {
        shoppingCartItem.refresh(product);
        shoppingCartItem.changeQuantity(shoppingCartItem.quantity().add(quantity));
    }

    private void insertItem(ShoppingCartItem shoppingCartItem) {
        this.items.add(shoppingCartItem);
    }

    private Optional<ShoppingCartItem> searchItemByProduct(ProductId productId) {
        Objects.requireNonNull(productId);
        return this.items.stream()
                .filter(i -> i.productId().equals(productId))
                .findFirst();
    }

    private void recalculateTotals() {
        BigDecimal totalAmount = items.stream()
                .map(i -> i.totalAmount().value())
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        Integer totalItems = items.stream()
                .map(i -> i.quantity().value())
                .reduce(0, Integer::sum);

        this.totalAmount = new Money(totalAmount);
        this.totalItems = new Quantity(totalItems);
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
}
