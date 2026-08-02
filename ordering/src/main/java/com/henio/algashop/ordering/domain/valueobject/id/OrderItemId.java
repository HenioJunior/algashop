package com.henio.algashop.ordering.domain.valueobject.id;

import com.henio.algashop.ordering.domain.utility.IdGenerator;
import io.hypersistence.tsid.TSID;

import java.util.Objects;

public record OrderItemId(TSID value) {

    public OrderItemId {
        Objects.requireNonNull(
                value,
                "OrderItem ID cannot be null."
        );
    }

    public OrderItemId(long value) {
        this(TSID.from(value));
    }

    public OrderItemId(String value) {
        this(requireValidString(value));
    }

    public static OrderItemId generate() {
        return new OrderItemId(
                IdGenerator.generateTSID()
        );
    }

    private static TSID requireValidString(String value) {
        Objects.requireNonNull(
                value,
                "OrderItem ID cannot be null."
        );

        return TSID.from(value);
    }

    @Override
    public String toString() {
        return value.toString();
    }
}
