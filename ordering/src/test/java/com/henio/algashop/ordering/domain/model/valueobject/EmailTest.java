package com.henio.algashop.ordering.domain.model.valueobject;

import com.henio.algashop.ordering.domain.model.exception.DomainException;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;

class EmailTest {

    @Test
    void givenValidEmail_whenCreateEmail_shouldCreateSuccessfully() {
        Email email = new Email("bob.green@email.com");

        assertThat(email.value())
                .isEqualTo("bob.green@email.com");
    }

    @Test
    void givenNullEmail_whenCreateEmail_shouldThrowException() {
        assertThatNullPointerException()
                .isThrownBy(() -> new Email(null));
    }

    @Test
    void givenBlankEmail_whenCreateEmail_shouldThrowException() {
        assertThatExceptionOfType(DomainException.class)
                .isThrownBy(() -> new Email(" "));
    }

    @Test
    void givenEmailWithoutAtSign_whenCreateEmail_shouldThrowException() {
        assertThatExceptionOfType(DomainException.class)
                .isThrownBy(() -> new Email("bob.greenemail.com"));
    }

    @Test
    void givenEmailWithoutDomain_whenCreateEmail_shouldThrowException() {
        assertThatExceptionOfType(DomainException.class)
                .isThrownBy(() -> new Email("bob.green@"));
    }

    @Test
    void givenEmailWithoutLocalPart_whenCreateEmail_shouldThrowException() {
        assertThatExceptionOfType(DomainException.class)
                .isThrownBy(() -> new Email("@email.com"));
    }

    @Test
    void givenEmail_whenConvertToString_shouldReturnEmailAddress() {
        Email email = new Email("bob.green@email.com");

        assertThat(email.toString())
                .isEqualTo("bob.green@email.com");
    }
}
