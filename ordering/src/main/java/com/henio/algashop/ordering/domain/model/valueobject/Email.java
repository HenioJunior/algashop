package com.henio.algashop.ordering.domain.model.valueobject;

import com.henio.algashop.ordering.domain.model.exception.DomainException;
import com.henio.algashop.ordering.domain.model.validation.EmailValidator;

import java.util.Locale;
import java.util.Objects;

import static com.henio.algashop.ordering.domain.model.exception.CustomerMessages.*;

public record Email(String value) {
    public Email(String value) {
        Objects.requireNonNull(value, EMAIL_IS_REQUIRED);

        String normalizedEmail = value
                .trim()
                .toLowerCase(Locale.ROOT);

        if (normalizedEmail.isBlank()) {
            throw new DomainException(EMAIL_CANNOT_BE_BLANK);
        }

        if (!EmailValidator.isValid(normalizedEmail)) {
            throw new DomainException(EMAIL_IS_INVALID);
        }
        this.value = value.toLowerCase();
    }

    @Override
    public String toString() {
        return value;
    }
}
