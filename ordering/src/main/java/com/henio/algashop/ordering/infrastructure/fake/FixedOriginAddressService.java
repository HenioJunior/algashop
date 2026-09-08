package com.henio.algashop.ordering.infrastructure.fake;

import com.henio.algashop.ordering.domain.model.valueobject.Address;
import com.henio.algashop.ordering.domain.model.valueobject.ZipCode;
import org.springframework.stereotype.Component;
import com.henio.algashop.ordering.domain.model.service.OriginAddress;

@Component
public class FixedOriginAddressService implements OriginAddress {
    @Override
    public Address originAddress() {
        return Address.builder()
                .street("Bourbon Street")
                .number("1134")
                .neighborhood("North Ville")
                .city("York")
                .state("South California")
                .zipCode(new ZipCode("12345"))
                .build();
    }
}
