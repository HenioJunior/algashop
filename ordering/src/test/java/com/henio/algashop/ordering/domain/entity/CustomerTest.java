package com.henio.algashop.ordering.domain.entity;


import com.henio.algashop.ordering.domain.utility.IdGenerator;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.OffsetDateTime;

class CustomerTest {

    @Test
    void given_invalidEmail_whenTryCreateCustomer_shouldGenerateException() {
        Assertions.assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(
                () -> new Customer(
                        IdGenerator.generateTimeBasedUUID(),
                        "Bob Green",
                        LocalDate.of(1975, 7, 21),
                        "invalid",
                        "222-2692",
                        "041.365.698-99",
                        false,
                        OffsetDateTime.now()
                )
        );
    }

    @Test
    void given_invalidEmail_whenTryUpdatedCustomerEmail_shouldGenerateException() {
        Customer customer = new Customer(
                IdGenerator.generateTimeBasedUUID(),
                "Bob Green",
                LocalDate.of(1975, 7, 21),
                "bobgreen@email.com",
                "222-2692",
                "041.365.698-99",
                false,
                OffsetDateTime.now());

        Assertions.assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() ->
                        customer.changeEmail("invalid")
                );

    }

    @Test
    void given_unarchivedCustomer_whenActive_shouldAnonymize(){
        Customer customer = new Customer(
                IdGenerator.generateTimeBasedUUID(),
                "Bob Green",
                LocalDate.of(1975, 7, 21),
                "bobgreen@email.com",
                "222-2692",
                "041.365.698-99",
                false,
                OffsetDateTime.now());

        customer.archive();

        Assertions.assertThat(customer.isArchived()).isTrue();
        Assertions.assertThat(customer.fullName()).isEqualTo("Anonymous");
        Assertions.assertThat(customer.email()).isNotEqualTo("bobgreen@email.com");
        Assertions.assertThat(customer.birthDate()).isNull();
        Assertions.assertThat(customer.phone()).isEqualTo("0");
        Assertions.assertThat(customer.document()).isEqualTo("0");
    }
}