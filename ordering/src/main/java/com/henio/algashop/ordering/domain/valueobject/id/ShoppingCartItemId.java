package com.henio.algashop.ordering.domain.valueobject.id;

import com.henio.algashop.ordering.domain.utility.IdGenerator;
import io.hypersistence.tsid.TSID;

import java.util.Objects;

public record ShoppingCartItemId(TSID value) {

    public ShoppingCartItemId {
        Objects.requireNonNull(value,"Shopping cart item id cannot be null.");
    }

    public ShoppingCartItemId() {
        this(IdGenerator.generateTSID());
    }

    public static ShoppingCartItemId generate() {
        return new ShoppingCartItemId(IdGenerator.generateTSID());
    }
}
