package com.henio.algashop.ordering.domain.model.valueobject;

public class AddressTestDataBuilder {

    private AddressTestDataBuilder() {}

    public static Address.AddressBuilder anAddress() {
        return Address.builder()
                .street("Bourbon Street")
                .number("1234")
                .neighborhood("North Ville")
                .complement("apt. 11")
                .city("Montfort")
                .state("South Carolina")
                .zipCode(new ZipCode("79911"));
    }
}
