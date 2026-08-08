package com.henio.algashop.ordering.domain.model.valueobject;

import com.henio.algashop.ordering.domain.model.exception.DomainException;

import java.util.Objects;

import static com.henio.algashop.ordering.domain.model.exception.CustomerMessages.PHONE_NUMBER_CANNOT_BE_BLANK;
import static com.henio.algashop.ordering.domain.model.exception.CustomerMessages.PHONE_NUMBER_IS_REQUIRED;

public record Phone(String value) {

    public Phone(String value) {
        Objects.requireNonNull(value, PHONE_NUMBER_IS_REQUIRED);

        if(value.isBlank()) {
            throw new DomainException(PHONE_NUMBER_CANNOT_BE_BLANK);
        }

        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }
}
