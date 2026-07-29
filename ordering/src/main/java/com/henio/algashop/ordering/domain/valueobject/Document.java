package com.henio.algashop.ordering.domain.valueobject;

import com.henio.algashop.ordering.domain.exception.DomainException;

import java.util.Objects;

import static com.henio.algashop.ordering.domain.exception.CustomerMessages.DOCUMENT_CANNOT_BE_BLANK;
import static com.henio.algashop.ordering.domain.exception.CustomerMessages.DOCUMENT_IS_REQUIRED;

public record Document(String value) {

    public Document(String value) {
        Objects.requireNonNull(value, DOCUMENT_IS_REQUIRED);

        if(value.isBlank()) {
            throw new DomainException(DOCUMENT_CANNOT_BE_BLANK);
        }
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }
}
