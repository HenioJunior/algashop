package com.henio.algashop.ordering.domain.model.valueobject;

public class BillingTestDataBuilder {

    private BillingTestDataBuilder() {}

    public static Billing.BillingBuilder aBilling() {
        return Billing.builder()
                .address(AddressTestDataBuilder.anAddress().build())
                .document(new Document("225-09-1992"))
                .phone(new Phone("123-111-9911"))
                .email(new Email("john.doe@email.com"))
                .fullName(new FullName("John", "Doe"));
    }
}
