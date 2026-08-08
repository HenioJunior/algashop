package com.henio.algashop.ordering.domain.model.valueobject.id;

import com.henio.algashop.ordering.domain.model.utility.IdGenerator;
import io.hypersistence.tsid.TSID;

import java.util.Objects;

public record CustomerId(TSID value) {

    public CustomerId {
        Objects.requireNonNull(
                value,
                "Customer ID cannot be null."
        );
    }

    public CustomerId() {
        this(IdGenerator.generateTSID());
    }

    public static CustomerId generate() {
        return new CustomerId(
                IdGenerator.generateTSID()
        );
    }

    @Override
    public String toString() {
        return value.toString();
    }
}
