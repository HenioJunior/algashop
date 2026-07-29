package com.henio.algashop.ordering.domain.entity;


import com.henio.algashop.ordering.domain.exception.CustomerArchivedException;
import com.henio.algashop.ordering.domain.exception.DomainException;
import com.henio.algashop.ordering.domain.valueobject.*;
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
                        new BirthDate(LocalDate.of(1975, 7, 21)),
                        new Email("invalid"),
                        new Phone("222-2692"),
                        new Document("041.365.698-99"),
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
                new BirthDate(LocalDate.of(1975, 7, 21)),
                new Email("bob.green@email.com"),
                new Phone("222-2692"),
                new Document("041.365.698-99"),
                false,
                OffsetDateTime.now());

        Assertions.assertThatExceptionOfType(DomainException.class)
                .isThrownBy(() ->
                        customer.changeEmail(new Email("invalid"))
                );

    }

    @Test
    void given_unarchivedCustomer_whenActive_shouldAnonymize(){
        Customer customer = new Customer(
                new CustomerId(),
                new FullName("Bob", "Green"),
                new BirthDate(LocalDate.of(1975, 7, 21)),
                new Email("bob.green@email.com"),
                new Phone("222-2692"),
                new Document("041.365.698-99"),
                false,
                OffsetDateTime.now());

        customer.archive();

        Assertions.assertThat(customer.isArchived()).isTrue();
        Assertions.assertThat(customer.fullName()).isEqualTo(new FullName("Anonymous", "Customer"));
        Assertions.assertThat(customer.email().value()).endsWith("@anonymous.invalid");
        Assertions.assertThat(customer.birthDate()).isNull();
        Assertions.assertThat(customer.phone()).isNull();
        Assertions.assertThat(customer.document()).isNull();
    }

    @Test
    void given_archivedCustomer_whenTryToUpdate_shouldGenerateException() {
        Customer customer = new Customer(
                new CustomerId(),
                new FullName("Bob", "Green"),
                new BirthDate(LocalDate.of(1975, 7, 21)),
                new Email("bob.green@email.com"),
                new Phone("222-2692"),
                new Document("041.365.698-99"),
                false,
                OffsetDateTime.now()
        );

        customer.archive();

        Assertions.assertThatExceptionOfType(CustomerArchivedException.class)
                .isThrownBy(customer::archive);

        Assertions.assertThatExceptionOfType(CustomerArchivedException.class)
                .isThrownBy(() -> customer.changeEmail(new Email("email@gmail.com")));

        Assertions.assertThatExceptionOfType(CustomerArchivedException.class)
                .isThrownBy(() -> customer.changePhone(new Phone("223-2693")));

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
                new BirthDate(LocalDate.of(1975, 7, 21)),
                new Email("bob.green@email.com"),
                new Phone("222-2692"),
                new Document("041.365.698-99"),
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
                new BirthDate(LocalDate.of(1975, 7, 21)),
                new Email("bob.green@email.com"),
                new Phone("222-2692"),
                new Document("041.365.698-99"),
                false,
                OffsetDateTime.now()
        );

        Assertions.assertThatExceptionOfType(DomainException.class)
                .isThrownBy(()-> customer.addLoyaltyPoints(0));

        Assertions.assertThatExceptionOfType(DomainException.class)
                .isThrownBy(()-> customer.addLoyaltyPoints(-10));
    }
}
