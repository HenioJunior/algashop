package com.henio.algashop.ordering.domain.model.valueobject;

import com.henio.algashop.ordering.domain.model.exception.DomainException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.Period;

import static com.henio.algashop.ordering.domain.model.exception.CustomerMessages.BIRTH_DATE_IS_REQUIRED;
import static com.henio.algashop.ordering.domain.model.exception.CustomerMessages.BIRTH_DATE_MUST_BE_IN_PAST;

class BirthDateTest {

    @Test
    void givenValidDate_whenCreateBirthDate_shouldCreateSuccessfully() {
        LocalDate date = LocalDate.of(1991, 7, 5);

        BirthDate birthDate = new BirthDate(date);

        Assertions.assertThat(birthDate.value())
                .isEqualTo(date);
    }

    @Test
    void givenNullDate_whenCreateBirthDate_shouldThrowException() {
        Assertions.assertThatNullPointerException()
                .isThrownBy(() -> new BirthDate(null))
                .withMessage(BIRTH_DATE_IS_REQUIRED);
    }

    @Test
    void givenFutureDate_whenCreateBirthDate_shouldThrowException() {
        LocalDate futureDate = LocalDate.now().plusDays(1);

        Assertions.assertThatExceptionOfType(DomainException.class)
                .isThrownBy(() -> new BirthDate(futureDate))
                .withMessage(BIRTH_DATE_MUST_BE_IN_PAST);
    }

    @Test
    void givenPastBirthDate_whenCalculateAge_shouldReturnCorrectAge() {
        LocalDate date = LocalDate.now()
                .minusYears(30)
                .minusDays(1);

        BirthDate birthDate = new BirthDate(date);

        int expectedAge = Period.between(
                date,
                LocalDate.now()
        ).getYears();

        Assertions.assertThat(birthDate.age())
                .isEqualTo(expectedAge);
    }

    @Test
    void givenBirthDate_whenConvertToString_shouldReturnIsoDate() {
        BirthDate birthDate =
                new BirthDate(LocalDate.of(1991, 7, 5));

        Assertions.assertThat(birthDate.toString())
                .isEqualTo("1991-07-05");
    }
}
