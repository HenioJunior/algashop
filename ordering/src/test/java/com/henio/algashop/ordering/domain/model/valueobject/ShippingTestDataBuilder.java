package com.henio.algashop.ordering.domain.model.valueobject;

import java.time.LocalDate;

public class ShippingTestDataBuilder {

    private ShippingTestDataBuilder() {}

    public static Shipping.ShippingBuilder aShipping() {
        return Shipping.builder()
                .address(AddressTestDataBuilder.anAddress().build())
                .recipient(
                        Recipient.builder()
                                .fullName(new FullName("John", "Doe"))
                                .document(new Document("112-33-2321"))
                                .phone(new Phone("111-441-1244"))
                                .build())
                .cost(new Money("10.00"))
                .expectedDate(LocalDate.now().plusDays(1));
    }

    public static Shipping.ShippingBuilder aPastDateShipping() {
        return Shipping.builder()
                .address(AddressTestDataBuilder.anAddress().build())
                .recipient(
                        Recipient.builder()
                                .fullName(new FullName("John", "Doe"))
                                .document(new Document("112-33-2321"))
                                .phone(new Phone("111-441-1244"))
                                .build())
                .cost(new Money("10.00"))
                .expectedDate(LocalDate.now().minusDays(7));
    }
}
