package com.henio.algashop.ordering.domain.model.valueobject.id;

import com.henio.algashop.ordering.domain.model.utility.IdGenerator;
import io.hypersistence.tsid.TSID;

import java.util.Objects;

public record OrderId(TSID value) {

    public OrderId {
        Objects.requireNonNull(
                value,
                "Order ID cannot be null."
        );
    }

    public OrderId() {
        this(IdGenerator.generateTSID());
    }

    public static OrderId generate() {
        return new OrderId(
                IdGenerator.generateTSID()
        );
    }

    @Override
    public String toString() {
        return value.toString();
    }
}
