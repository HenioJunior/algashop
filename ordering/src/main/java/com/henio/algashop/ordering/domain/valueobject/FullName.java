package com.henio.algashop.ordering.domain.valueobject;

import com.henio.algashop.ordering.domain.exception.DomainException;

import java.util.Objects;

import static com.henio.algashop.ordering.domain.exception.CustomerMessages.*;

public record FullName(String firstName, String lastName) {

    public FullName(String firstName, String lastName) {
        Objects.requireNonNull(firstName, FIRST_NAME_IS_REQUIRED);
        Objects.requireNonNull(lastName, LAST_NAME_IS_REQUIRED);

        if(firstName.isBlank() || lastName.isBlank()) {
            throw new DomainException(FIRST_NAME_AND_LAST_NAME_CANNOT_BE_BLANK);
        }

        this.firstName = firstName.trim();
        this.lastName = lastName.trim();
    }

    @Override
    public String toString() {
        return firstName + " " + lastName;
    }
}
