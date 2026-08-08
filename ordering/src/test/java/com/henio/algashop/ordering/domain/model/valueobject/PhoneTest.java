package com.henio.algashop.ordering.domain.model.valueobject;

import com.henio.algashop.ordering.domain.model.exception.DomainException;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;

class PhoneTest {

    @Test
    void givenValidPhone_whenCreatePhone_shouldCreateSuccessfully() {
        Phone phone = new Phone("222-2692");

        assertThat(phone.value())
                .isEqualTo("222-2692");
    }

    @Test
    void givenNullPhone_whenCreatePhone_shouldThrowException() {
        assertThatNullPointerException()
                .isThrownBy(() -> new Phone(null));
    }

    @Test
    void givenEmptyPhone_whenCreatePhone_shouldThrowException() {
        assertThatExceptionOfType(DomainException.class)
                .isThrownBy(() -> new Phone(""));
    }

    @Test
    void givenBlankPhone_whenCreatePhone_shouldThrowException() {
        assertThatExceptionOfType(DomainException.class)
                .isThrownBy(() -> new Phone(" "));
    }

    @Test
    void givenPhone_whenConvertToString_shouldReturnPhoneNumber() {
        Phone phone = new Phone("222-2692");

        assertThat(phone.toString())
                .isEqualTo("222-2692");
    }
}
