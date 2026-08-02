package com.henio.algashop.ordering.domain.valueobject.id;

import com.henio.algashop.ordering.domain.utility.IdGenerator;
import io.hypersistence.tsid.TSID;

import java.util.Objects;

public record ProductId(TSID value) {

    public ProductId {
        Objects.requireNonNull(
                value,
                "Product ID cannot be null."
        );
    }

    public ProductId(long value) {
        this(TSID.from(value));
    }

    public ProductId(String value) {
        this(requireValidString(value));
    }

    public static ProductId generate() {
        return new ProductId(
                IdGenerator.generateTSID()
        );
    }

    private static TSID requireValidString(String value) {
        Objects.requireNonNull(
                value,
                "Product ID cannot be null."
        );

        return TSID.from(value);
    }

    @Override
    public String toString() {
        return value.toString();
    }
}
