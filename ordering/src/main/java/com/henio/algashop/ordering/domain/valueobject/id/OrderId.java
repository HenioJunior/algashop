package com.henio.algashop.ordering.domain.valueobject.id;

import com.henio.algashop.ordering.domain.utility.IdGenerator;
import io.hypersistence.tsid.TSID;

import java.util.Objects;

public record OrderId(TSID value) {

    public OrderId {
        Objects.requireNonNull(
                value,
                "Order ID cannot be null."
        );
    }

    public OrderId(long value) {
        this(TSID.from(value));
    }

    public OrderId(String value) {
        this(requireValidString(value));
    }

    public static OrderId generate() {
        return new OrderId(
                IdGenerator.generateTSID()
        );
    }

    private static TSID requireValidString(String value) {
        Objects.requireNonNull(
                value,
                "Order ID cannot be null."
        );

        return TSID.from(value);
    }

    @Override
    public String toString() {
        return value.toString();
    }
}
