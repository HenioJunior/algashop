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

        if (quantity.compareTo(Quantity.ZERO) <= 0) {
            throw new IllegalArgumentException(
                    "Quantity must be greater than zero."
            );
        }
        return new Money(value.multiply(new BigDecimal(quantity.value())));
    }


    public Money add(Money other) {
        Objects.requireNonNull(other, "Money cannot be null");
        return new Money(value.add(other.value));
    }

    public Money divide(int divisor) {
        if (divisor == 0) {
            throw new IllegalArgumentException("Divisor cannot be zero");
        }
        return new Money(
                value.divide(
                        BigDecimal.valueOf(divisor),
                        2,
                        roundingMode
                ));
    }

    @Override
    public int compareTo(Money other) {
        Objects.requireNonNull(other, "Money cannot be null");
        return value.compareTo(other.value);
    }

    @Override
    public String toString() {
        return value.toString();
    }
}
