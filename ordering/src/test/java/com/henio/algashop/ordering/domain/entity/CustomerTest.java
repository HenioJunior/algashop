package com.henio.algashop.ordering.domain.entity;


import com.henio.algashop.ordering.domain.exception.CustomerArchivedException;
import com.henio.algashop.ordering.domain.exception.DomainException;
import com.henio.algashop.ordering.domain.valueobject.CustomerId;
import com.henio.algashop.ordering.domain.valueobject.FullName;
import com.henio.algashop.ordering.domain.valueobject.LoyaltyPoints;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.OffsetDateTime;

class CustomerTest {

    @Test
    void given_invalidEmail_whenTryCreateCustomer_shouldGenerateException() {
        Assertions.assertThatExceptionOfType(DomainException.class).isThrownBy(
                () -> new Customer(
                        new CustomerId(),
                        new FullName("Bob", "Green"),
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
                new CustomerId(),
                new FullName("Bob", "Green"),
                LocalDate.of(1975, 7, 21),
                "bobgreen@email.com",
                "222-2692",
                "041.365.698-99",
                false,
                OffsetDateTime.now());

        Assertions.assertThatExceptionOfType(DomainException.class)
                .isThrownBy(() ->
                        customer.changeEmail("invalid")
                );

    }

    @Test
    void given_unarchivedCustomer_whenActive_shouldAnonymize(){
        Customer customer = new Customer(
                new CustomerId(),
                new FullName("Bob", "Green"),
                LocalDate.of(1975, 7, 21),
                "bobgreen@email.com",
                "222-2692",
                "041.365.698-99",
                false,
                OffsetDateTime.now());

        customer.archive();

        Assertions.assertThat(customer.isArchived()).isTrue();
        Assertions.assertThat(customer.fullName()).isEqualTo(new FullName("Anonymous", "Customer"));
        Assertions.assertThat(customer.email()).isNotEqualTo("bobgreen@email.com");
        Assertions.assertThat(customer.birthDate()).isNull();
        Assertions.assertThat(customer.phone()).isEqualTo(null);
        Assertions.assertThat(customer.document()).isEqualTo(null);
    }

    @Test
    void given_archivedCustomer_whenTryToUpdate_shouldGenerateException() {
        Customer customer = new Customer(
                new CustomerId(),
                new FullName("Bob", "Green"),
                LocalDate.of(1991, 7, 5),
                "bob.green@gmail.com",
                "478-256-2504",
                "255-08-0578",
                false,
                OffsetDateTime.now()
        );

        customer.archive();

        Assertions.assertThatExceptionOfType(CustomerArchivedException.class)
                .isThrownBy(customer::archive);

        Assertions.assertThatExceptionOfType(CustomerArchivedException.class)
                .isThrownBy(() -> customer.changeEmail("email@gmail.com"));

        Assertions.assertThatExceptionOfType(CustomerArchivedException.class)
                .isThrownBy(() -> customer.changePhone("123-123-1111"));

        Assertions.assertThatExceptionOfType(CustomerArchivedException.class)
                .isThrownBy(customer::enablePromotionNotifications);

        Assertions.assertThatExceptionOfType(CustomerArchivedException.class)
                .isThrownBy(customer::disablePromotionNotifications);
    }

    @Test
    void given_brandNewCustomer_whenAddLoyaltyPoints_shouldSumPoints() {
        Customer customer = new Customer(
                new CustomerId(),
                new FullName("Bob", "Green"),
                LocalDate.of(1991, 7, 5),
                "bob.green@gmail.com",
                "478-256-2504",
                "255-08-0578",
                false,
                OffsetDateTime.now()
        );

        customer.addLoyaltyPoints(10);
        customer.addLoyaltyPoints(20);

        Assertions.assertThat(customer.loyaltyPoints()).isEqualTo(new LoyaltyPoints(30));
    }

    @Test
    void given_brandNewCustomer_whenAddInvalidLoyaltyPoints_shouldGenerateException() {
        Customer customer = new Customer(
                new CustomerId(),
                new FullName("Bob", "Green"),
                LocalDate.of(1991, 7, 5),
                "bob.green@gmail.com",
                "478-256-2504",
                "255-08-0578",
                false,
                OffsetDateTime.now()
        );

        Assertions.assertThatExceptionOfType(DomainException.class)
                .isThrownBy(()-> customer.addLoyaltyPoints(0));

        Assertions.assertThatExceptionOfType(DomainException.class)
                .isThrownBy(()-> customer.addLoyaltyPoints(-10));
    }
}
