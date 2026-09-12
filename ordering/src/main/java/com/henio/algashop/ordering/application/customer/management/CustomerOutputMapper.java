package com.henio.algashop.ordering.application.customer.management;

import com.henio.algashop.ordering.application.commons.AddressData;
import com.henio.algashop.ordering.domain.model.commons.Address;
import com.henio.algashop.ordering.domain.model.customer.Customer;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class CustomerOutputMapper {

    public CustomerOutput fromDomain(Customer customer) {
        Objects.requireNonNull(customer, "Customer is required");

        return CustomerOutput.builder()
                .id(customer.id().toString())
                .firstName(customer.fullName().firstName())
                .lastName(customer.fullName().lastName())
                .email(customer.email().value())
                .document(customer.document() == null
                ? null
                : customer.document().value())
                .phone(customer.phone() == null
                ? null
                : customer.phone().value())
                .promotionNotificationsAllowed(
                        customer.isPromotionNotificationsAllowed()
                )
                .loyaltyPoints(customer.loyaltyPoints().value())
                .registeredAt(customer.registeredAt())
                .archived(customer.isArchived())
                .archivedAt(customer.archivedAt())
                .birthDate(
                        customer.birthDate() != null
                                ? customer.birthDate().value()
                                : null
                )
                .address(toAddressData(customer.address()))
                .build();
    }

    private AddressData toAddressData(Address address) {
        if (address == null) {
            return null;
        }

        return AddressData.builder()
                .street(address.street())
                .number(address.number())
                .complement(address.complement())
                .neighborhood(address.neighborhood())
                .city(address.city())
                .state(address.state())
                .zipCode(address.zipCode().value())
                .build();
    }
}
