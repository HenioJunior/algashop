package com.henio.algashop.ordering.domain.model.commons;

import com.henio.algashop.ordering.domain.model.shared.DomainException;

import java.util.Objects;

import static com.henio.algashop.ordering.domain.model.validation.ErrorMessages.DOCUMENT_CANNOT_BE_BLANK;
import static com.henio.algashop.ordering.domain.model.validation.ErrorMessages.DOCUMENT_IS_REQUIRED;

public record Document(String value) {

    public Document {
        Objects.requireNonNull(value, DOCUMENT_IS_REQUIRED);

        if (value.isBlank()) {
            throw new DomainException(DOCUMENT_CANNOT_BE_BLANK);
        }
    }

    @Override
    public String toString() {
        return value;
    }
}
