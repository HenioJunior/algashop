package com.henio.algashop.ordering.domain.model.product;

import com.henio.algashop.ordering.domain.model.shared.IdGenerator;
import io.hypersistence.tsid.TSID;

import java.util.Objects;

public record ProductId(TSID value) {

    public ProductId {
        Objects.requireNonNull(
                value,
                "Product ID cannot be null."
        );
    }

    public ProductId() {
        this(IdGenerator.generateTSID());
    }

    public static ProductId generate() {
        return new ProductId(
                IdGenerator.generateTSID()
        );
    }

    @Override
    public String toString() {
        return value.toString();
    }
}
