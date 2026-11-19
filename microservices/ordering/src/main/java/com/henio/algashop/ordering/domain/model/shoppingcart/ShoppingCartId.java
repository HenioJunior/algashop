package com.henio.algashop.ordering.domain.model.shoppingcart;

import com.henio.algashop.ordering.domain.model.shared.IdGenerator;

import java.util.Objects;
import java.util.UUID;

public record ShoppingCartId(UUID value) {

    public ShoppingCartId {
        Objects.requireNonNull(value,"Shopping cart id cannot be null.");
    }

    public ShoppingCartId () {
        this(IdGenerator.generateTimeBasedUUID());
    }

    public static ShoppingCartId generate() {
        return new ShoppingCartId(IdGenerator.generateTimeBasedUUID());
    }
}
