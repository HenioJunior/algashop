package com.henio.algashop.ordering.domain.model.validation;

import com.henio.algashop.ordering.domain.model.exception.DomainException;

import java.util.Objects;

import static com.henio.algashop.ordering.domain.model.exception.CustomerMessages.FIELD_CANNOT_BE_BLANK;
import static com.henio.algashop.ordering.domain.model.exception.CustomerMessages.FIELD_IS_REQUIRED;

public final class FieldValidator {

    private FieldValidator() {
        throw new AssertionError("Utility class");
    }

    public static void requireNonNull(Object value) {
        requireNonNull(value, FIELD_IS_REQUIRED);
    }

    public static void requireNonNull(Object value, String message) {
        Objects.requireNonNull(value, message);
    }

    public static void requireNonBlank(String value) {
        requireNonBlank(value, FIELD_CANNOT_BE_BLANK);
    }

    public static void requireNonBlank(String value, String message) {
        requireNonNull(value);

        if (value.isBlank()) {
            throw new DomainException(message);
        }
    }
}
