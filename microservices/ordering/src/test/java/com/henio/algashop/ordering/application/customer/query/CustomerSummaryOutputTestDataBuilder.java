package com.henio.algashop.ordering.application.customer.query;

import com.henio.algashop.ordering.domain.model.customer.CustomerId;

import java.time.LocalDate;
import java.time.OffsetDateTime;

public class CustomerSummaryOutputTestDataBuilder {

    public static CustomerSummaryOutput.CustomerSummaryOutputBuilder existing() {
        return CustomerSummaryOutput.builder()
                .id(new CustomerId().toString())
                .registeredAt(OffsetDateTime.now())
                .phone("1191234564")
                .email("johndoe@email.com")
                .firstName("John")
                .lastName("Doe")
                .birthDate(LocalDate.of(1991, 7, 5))
                .document("12345")
                .promotionNotificationsAllowed(false)
                .loyaltyPoints(0);
    }

    public static CustomerSummaryOutput.CustomerSummaryOutputBuilder existingAlt1() {
        return CustomerSummaryOutput.builder()
                .id(new CustomerId().toString())
                .registeredAt(OffsetDateTime.now())
                .phone("119123456")
                .email("scott1977@email.com")
                .firstName("Scott")
                .lastName("Stacey")
                .birthDate(LocalDate.of(1977, 1, 5))
                .document("98745")
                .promotionNotificationsAllowed(true)
                .loyaltyPoints(10);
    }

}
