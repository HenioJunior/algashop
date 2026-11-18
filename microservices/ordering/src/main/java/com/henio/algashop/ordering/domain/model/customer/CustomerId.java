package com.henio.algashop.ordering.domain.model.customer;

import com.henio.algashop.ordering.domain.model.shared.IdGenerator;

import java.util.Objects;
import java.util.UUID;

public record CustomerId(UUID value) {

    public CustomerId {
        Objects.requireNonNull(
                value,
                "Customer ID cannot be null."
        );
    }

    public CustomerId() {
        this(IdGenerator.generateTimeBasedUUID());
    }

    public static CustomerId generate() {
        return new CustomerId(
                IdGenerator.generateTimeBasedUUID()
        );
    }

    @Override
    public String toString() {
        return value.toString();
    }
}
