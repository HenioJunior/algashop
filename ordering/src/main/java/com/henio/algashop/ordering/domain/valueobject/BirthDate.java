package com.henio.algashop.ordering.domain.valueobject;

import com.henio.algashop.ordering.domain.exception.DomainException;

import java.time.LocalDate;
import java.time.Period;
import java.util.Objects;

import static com.henio.algashop.ordering.domain.exception.CustomerMessages.BIRTH_DATE_IS_REQUIRED;
import static com.henio.algashop.ordering.domain.exception.CustomerMessages.BIRTH_DATE_MUST_BE_IN_PAST;

public record BirthDate(LocalDate value) {

    public BirthDate {
        Objects.requireNonNull(value, BIRTH_DATE_IS_REQUIRED);

        if(value.isAfter(LocalDate.now())) {
            throw new DomainException(BIRTH_DATE_MUST_BE_IN_PAST);
        }
    }

    public int age() {
        return Period.between(value, LocalDate.now()).getYears();
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }
}
