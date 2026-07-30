package com.henio.algashop.ordering.domain.entity;

import com.henio.algashop.ordering.domain.exception.CustomerArchivedException;
import com.henio.algashop.ordering.domain.exception.DomainException;
import com.henio.algashop.ordering.domain.valueobject.*;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class CustomerTest {

    @Test
    void given_invalidEmail_whenTryCreateCustomer_shouldGenerateException() {

        Assertions.assertThatExceptionOfType(DomainException.class).isThrownBy(
                () -> Customer.createCustomer(
                        new FullName("Bob", "Green"),
                        new BirthDate(LocalDate.of(1975, 7, 21)),
                        new Email("invalid"),
                        new Phone("222-2692"),
                        new Document("041.365.698-99"),
                        false,
                        Address.builder()
                                .street("Bourbon Street")
                                .number("1134")
                                .neighborhood("North Ville")
                                .city("York")
                                .state("South California")
                                .zipCode(new ZipCode("12345"))
                                .complement("Apt. 114")
                                .build()
                )
        );
    }

    @Test
    void given_invalidEmail_whenTryUpdatedCustomerEmail_shouldGenerateException() {
        Customer customer = Customer.createCustomer(
                new FullName("Bob", "Green"),
                new BirthDate(LocalDate.of(1975, 7, 21)),
                new Email("bob.green@email.com"),
                new Phone("222-2692"),
                new Document("041.365.698-99"),
                false,
                Address.builder()
                        .street("Bourbon Street")
                        .number("1134")
                        .neighborhood("North Ville")
                        .city("York")
                        .state("South California")
                        .zipCode(new ZipCode("12345"))
                        .complement("Apt. 114")
                        .build()
                );

        Assertions.assertThatExceptionOfType(DomainException.class)
                .isThrownBy(() ->
                        customer.changeEmail(new Email("invalid"))
                );

    }

    @Test
    void given_unarchivedCustomer_whenActive_shouldAnonymize(){
        Customer customer = Customer.createCustomer(
                new FullName("Bob", "Green"),
                new BirthDate(LocalDate.of(1975, 7, 21)),
                new Email("bob.green@email.com"),
                new Phone("222-2692"),
                new Document("041.365.698-99"),
                false,
                Address.builder()
                        .street("Bourbon Street")
                        .number("1134")
                        .neighborhood("North Ville")
                        .city("York")
                        .state("South California")
                        .zipCode(new ZipCode("12345"))
                        .complement("Apt. 114")
                        .build()
                );

        customer.archive();

        Assertions.assertWith(customer,
                c -> assertThat(c.fullName()).isEqualTo(new FullName("Anonymous", "Customer")),
                c -> assertThat(c.email().value()).endsWith("@anonymous.invalid"),
                c -> assertThat(c.birthDate()).isNull(),
                c -> assertThat(c.phone()).isNull(),
                c -> assertThat(c.document()).isNull(),
                c -> assertThat(c.address()).isEqualTo(Address.builder()
                        .street("Bourbon Street")
                        .number("Anonymized")
                        .neighborhood("North Ville")
                        .city("York")
                        .state("South California")
                        .zipCode(new ZipCode("12345"))
                        .complement(null)
                        .build())
        );
    }

    @Test
    void given_archivedCustomer_whenTryToUpdate_shouldGenerateException() {
        Customer customer = Customer.existing(
                new CustomerId(),
                new FullName("Anonymous", "Customer"),
                null,
                new Email(UUID.randomUUID() + "@anonymous.invalid"),
                null,
                null,
                false,
                true,
                OffsetDateTime.now(),
                OffsetDateTime.now(),
                new LoyaltyPoints(10),
                Address.builder()
                        .street("Bourbon Street")
                        .number("Anonymized")
                        .neighborhood("North Ville")
                        .city("York")
                        .state("South California")
                        .zipCode(new ZipCode("12345"))
                        .complement(null)
                        .build()
        );

        Assertions.assertThatExceptionOfType(CustomerArchivedException.class)
                .isThrownBy(customer::archive);

        Assertions.assertThatExceptionOfType(CustomerArchivedException.class)
                .isThrownBy(()-> customer.changeEmail(new Email("email@gmail.com")));

        Assertions.assertThatExceptionOfType(CustomerArchivedException.class)
                .isThrownBy(()-> customer.changePhone(new Phone("123-123-1111")));

        Assertions.assertThatExceptionOfType(CustomerArchivedException.class)
                .isThrownBy(customer::enablePromotionNotifications);

        Assertions.assertThatExceptionOfType(CustomerArchivedException.class)
                .isThrownBy(customer::disablePromotionNotifications);
    }

    @Test
    void given_brandNewCustomer_whenAddLoyaltyPoints_shouldSumPoints() {
        Customer customer = Customer.createCustomer(
                new FullName("Bob", "Green"),
                new BirthDate(LocalDate.of(1975, 7, 21)),
                new Email("bob.green@email.com"),
                new Phone("222-2692"),
                new Document("041.365.698-99"),
                false,
                Address.builder()
                        .street("Bourbon Street")
                        .number("1134")
                        .neighborhood("North Ville")
                        .city("York")
                        .state("South California")
                        .zipCode(new ZipCode("12345"))
                        .complement("Apt. 114")
                        .build()
        );

        customer.addLoyaltyPoints(10);
        customer.addLoyaltyPoints(20);

        Assertions.assertThat(customer.loyaltyPoints()).isEqualTo(new LoyaltyPoints(30));
    }

    @Test
    void given_brandNewCustomer_whenAddInvalidLoyaltyPoints_shouldGenerateException() {
        Customer customer = Customer.createCustomer(
                new FullName("Bob", "Green"),
                new BirthDate(LocalDate.of(1975, 7, 21)),
                new Email("bob.green@email.com"),
                new Phone("222-2692"),
                new Document("041.365.698-99"),
                false,
                Address.builder()
                        .street("Bourbon Street")
                        .number("1134")
                        .neighborhood("North Ville")
                        .city("York")
                        .state("South California")
                        .zipCode(new ZipCode("12345"))
                        .complement("Apt. 114")
                        .build()
        );

        Assertions.assertThatExceptionOfType(DomainException.class)
                .isThrownBy(()-> customer.addLoyaltyPoints(0));

        Assertions.assertThatExceptionOfType(DomainException.class)
                .isThrownBy(()-> customer.addLoyaltyPoints(-10));
    }
}
