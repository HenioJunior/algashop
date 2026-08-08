package com.henio.algashop.ordering.domain.model.valueobject;

import lombok.Builder;

import java.time.LocalDate;
import java.util.Objects;

@Builder(toBuilder = true)
public record Shipping(Recipient recipient, Address address, Money cost, LocalDate expectedDate) {

    public Shipping {
    Objects.requireNonNull(recipient, "Shipping recipient is required");
    Objects.requireNonNull(address, "Shipping address is required");
    Objects.requireNonNull(cost, "Shipping cost is required");
    Objects.requireNonNull(expectedDate, "Expected date is required");
    }
}
