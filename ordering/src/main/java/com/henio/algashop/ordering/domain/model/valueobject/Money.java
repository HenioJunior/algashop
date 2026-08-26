package com.henio.algashop.ordering.domain.model.valueobject;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

public record Money(BigDecimal value) implements Comparable<Money> {

    private static final RoundingMode roundingMode = RoundingMode.HALF_EVEN;

    public static final Money ZERO = new Money(BigDecimal.ZERO);

    public Money {
        Objects.requireNonNull(value, "Money cannot be null");

        if(value.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Money cannot be negative");
        }
        value = value.setScale(2, roundingMode);
    }

    public Money(String value) {
        this(parse(value));
    }

    private static BigDecimal parse(String value) {
        Objects.requireNonNull(value, "Money cannot be null");
        return new BigDecimal(value);
    }

    public Money multiply(Quantity quantity) {
        Objects.requireNonNull(quantity, "Quantity cannot be null");

        if (quantity.value() < 1) {
            throw new IllegalArgumentException(
                    "Quantity must be greater than zero."
            );
        }
        BigDecimal multiplied = this.value.multiply(new BigDecimal(quantity.value()));
        return new Money(multiplied);
    }


    public Money add(Money other) {
        Objects.requireNonNull(other, "Money cannot be null");
        return new Money(value.add(other.value));
    }

    public Money divide(Money o) {
        if (Money.ZERO.equals(o)) {
            throw new IllegalArgumentException("Divisor cannot be zero");
        }
        return new Money(this.value.divide(o.value, roundingMode));
    }

    @Override
    public int compareTo(Money other) {
        Objects.requireNonNull(other, "Money cannot be null");
        return this.value.compareTo(other.value);
    }

    @Override
    public String toString() {
        return value.toString();
    }
}
