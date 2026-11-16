package com.henio.algashop.ordering.domain.model.product;

import com.henio.algashop.ordering.domain.model.shared.IdGenerator;

import java.util.Objects;
import java.util.UUID;

public record ProductId(UUID value) {

    public ProductId {
        Objects.requireNonNull(
                value,
                "Product ID cannot be null."
        );
    }

    public ProductId() {
        this(IdGenerator.generateTimeBasedUUID());
    }

    public static ProductId generate() {
        return new ProductId(IdGenerator.generateTimeBasedUUID());
    }

    @Override
    public String toString() {
        return value.toString();
    }
}
