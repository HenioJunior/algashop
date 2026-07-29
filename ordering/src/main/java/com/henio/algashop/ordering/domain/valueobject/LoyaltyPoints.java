package com.henio.algashop.ordering.domain.valueobject;

import com.henio.algashop.ordering.domain.exception.DomainException;

import java.util.Objects;

import static com.henio.algashop.ordering.domain.exception.CustomerMessages.LOYALTY_POINTS_CANNOT_BE_NEGATIVE;
import static com.henio.algashop.ordering.domain.exception.CustomerMessages.LOYALTY_POINTS_TO_ADD_MUST_BE_GREATER_THAN_ZERO;

public record LoyaltyPoints(int value) {

    public LoyaltyPoints {
        if (value < 0) {
            throw new DomainException(
                    LOYALTY_POINTS_CANNOT_BE_NEGATIVE
            );
        }
    }

    public LoyaltyPoints() {
        this(0);
    }

    public LoyaltyPoints add(LoyaltyPoints points) {
        Objects.requireNonNull(points);

        return add(points.value());
    }

    public LoyaltyPoints add(int points) {
        if (points <= 0) {
            throw new DomainException(
                    LOYALTY_POINTS_TO_ADD_MUST_BE_GREATER_THAN_ZERO
            );
        }

        return new LoyaltyPoints(
                Math.addExact(this.value, points)
        );
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }
}
