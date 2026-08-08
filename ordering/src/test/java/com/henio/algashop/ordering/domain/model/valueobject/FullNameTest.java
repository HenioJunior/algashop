package com.henio.algashop.ordering.domain.model.valueobject;

import com.henio.algashop.ordering.domain.model.exception.DomainException;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;

class FullNameTest {

    @Test
    void givenValidNames_whenCreateFullName_shouldCreateSuccessfully() {
        FullName fullName = new FullName("Bob", "Green");

        assertThat(fullName.firstName())
                .isEqualTo("Bob");

        assertThat(fullName.lastName())
                .isEqualTo("Green");
    }

    @Test
    void givenNullFirstName_whenCreateFullName_shouldThrowException() {
        assertThatNullPointerException()
                .isThrownBy(() -> new FullName(null, "Green"));
    }

    @Test
    void givenNullLastName_whenCreateFullName_shouldThrowException() {
        assertThatNullPointerException()
                .isThrownBy(() -> new FullName("Bob", null));
    }

    @Test
    void givenBlankFirstName_whenCreateFullName_shouldThrowException() {
        assertThatExceptionOfType(DomainException.class)
                .isThrownBy(() -> new FullName(" ", "Green"));
    }

    @Test
    void givenBlankLastName_whenCreateFullName_shouldThrowException() {
        assertThatExceptionOfType(DomainException.class)
                .isThrownBy(() -> new FullName("Bob", " "));
    }

    @Test
    void givenFullName_whenConvertToString_shouldReturnCompleteName() {
        FullName fullName = new FullName("Bob", "Green");

        assertThat(fullName.toString())
                .isEqualTo("Bob Green");
    }
}
