package com.henio.algashop.ordering.domain.model.entity;

import com.henio.algashop.ordering.domain.model.valueobject.*;

import java.time.LocalDate;

public class CustomerTestDataFactory {

    private CustomerTestDataFactory() {
    }

    public static Customer createNewCustomer() {
        return Customer.create(
                new FullName("John", "Doe"),
                new BirthDate(LocalDate.of(1991, 7, 5)),
                new Email("johndoe@email.com"),
                new Phone("478-256-2604"),
                new Document("255-08-0578"),
                true,
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
    }
}
