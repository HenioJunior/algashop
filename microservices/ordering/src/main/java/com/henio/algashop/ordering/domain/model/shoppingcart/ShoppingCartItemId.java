package com.henio.algashop.ordering.domain.model.shoppingcart;

import com.henio.algashop.ordering.domain.model.shared.IdGenerator;

import java.util.Objects;
import java.util.UUID;

public record ShoppingCartItemId(UUID value) {

    public ShoppingCartItemId {
        Objects.requireNonNull(value,"Shopping cart item id cannot be null.");
    }

    public ShoppingCartItemId() {
        this(IdGenerator.generateTimeBasedUUID());
    }

    public static ShoppingCartItemId generate() {
        return new ShoppingCartItemId(IdGenerator.generateTimeBasedUUID());
    }
}
