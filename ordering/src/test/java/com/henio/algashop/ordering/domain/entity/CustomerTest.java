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
}