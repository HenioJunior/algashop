package com.henio.algashop.ordering.domain.valueobject.id;

import com.henio.algashop.ordering.domain.utility.IdGenerator;
import io.hypersistence.tsid.TSID;

import java.util.Objects;

public record ShoppingCartId(TSID value) {

    public ShoppingCartId {
        Objects.requireNonNull(value,"Shopping cart id cannot be null.");
    }

    public ShoppingCartId () {
        this(IdGenerator.generateTSID());
    }

    public static ShoppingCartId generate() {
        return new ShoppingCartId(IdGenerator.generateTSID());
    }
}
