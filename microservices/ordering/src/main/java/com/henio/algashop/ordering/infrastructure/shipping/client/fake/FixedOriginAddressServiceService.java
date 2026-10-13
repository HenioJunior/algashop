package com.henio.algashop.ordering.infrastructure.shipping.client.fake;

import com.henio.algashop.ordering.domain.model.commons.Address;
import com.henio.algashop.ordering.domain.model.commons.ZipCode;
import org.springframework.stereotype.Component;
import com.henio.algashop.ordering.domain.model.order.shipping.OriginAddressService;

@Component
public class FixedOriginAddressServiceService implements OriginAddressService {
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
