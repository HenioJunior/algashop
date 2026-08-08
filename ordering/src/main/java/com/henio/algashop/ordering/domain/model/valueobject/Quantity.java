package com.henio.algashop.ordering.domain.model.valueobject;

import java.util.Objects;

public record Quantity(Integer value) implements Comparable<Quantity> {

    public static final Quantity ZERO = new Quantity(0);

    public Quantity {
        Objects.requireNonNull(value, "Quantity cannot be null.");

        if(value < 0) {
            throw new IllegalArgumentException("Quantity cannot be negative.");
        }
    }

    public Quantity add(Quantity other) {
        Objects.requireNonNull(other, "Quantity cannot be null.");
        return new Quantity(Math.addExact(value, other.value));
    }

    @Override
    public int compareTo(Quantity other) {
        Objects.requireNonNull(other, "Quantity cannot be null.");
        return value.compareTo(other.value);
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }
}
