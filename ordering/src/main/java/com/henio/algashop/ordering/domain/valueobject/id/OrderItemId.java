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

    public OrderItemId() {
        this(IdGenerator.generateTSID());
    }

    public static OrderItemId generate() {
        return new OrderItemId(
                IdGenerator.generateTSID()
        );
    }

    @Override
    public String toString() {
        return value.toString();
    }
}
