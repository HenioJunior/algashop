package com.henio.algashop.ordering.domain.model.commons;

import com.henio.algashop.ordering.domain.model.FieldValidator;
import lombok.Builder;

@Builder(toBuilder = true)
public record Address(
        String street,
        String complement,
        String neighborhood,
        String number,
        String city,
        String state,
        ZipCode zipCode) {

    public Address {
        FieldValidator.requireNonBlank(street);
        FieldValidator.requireNonBlank(neighborhood);
        FieldValidator.requireNonBlank(number);
        FieldValidator.requireNonBlank(city);
        FieldValidator.requireNonBlank(state);
        FieldValidator.requireNonNull(zipCode);
    }
}


