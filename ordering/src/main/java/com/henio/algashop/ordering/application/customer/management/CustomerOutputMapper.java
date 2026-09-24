package com.henio.algashop.ordering.application.customer.management;

import com.henio.algashop.ordering.application.commons.AddressData;
import com.henio.algashop.ordering.application.customer.query.CustomerOutput;
import com.henio.algashop.ordering.domain.model.commons.Address;
import com.henio.algashop.ordering.domain.model.customer.Customer;
import com.henio.algashop.ordering.infrastructure.persistence.commons.AddressEmbeddable;
import com.henio.algashop.ordering.infrastructure.persistence.customer.CustomerPersistenceEntity;
import io.hypersistence.tsid.TSID;
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

    public CustomerOutput fromPersistence(CustomerPersistenceEntity entity) {
        Objects.requireNonNull(entity, "Customer persistence entity is required");

        return CustomerOutput.builder()
                .id(TSID.from(entity.getId()).toString())
                .firstName(entity.getFirstName())
                .lastName(entity.getLastName())
                .birthDate(entity.getBirthDate())
                .email(entity.getEmail())
                .phone(entity.getPhone())
                .document(entity.getDocument())
                .promotionNotificationsAllowed(
                        entity.isPromotionNotificationsAllowed()
                )
                .loyaltyPoints(entity.getLoyaltyPoints())
                .registeredAt(entity.getRegisteredAt())
                .archived(entity.isArchived())
                .archivedAt(entity.getArchivedAt())
                .address(toAddressData(entity.getAddress()))
                .build();
    }

    private AddressData toAddressData(AddressEmbeddable address) {
        Objects.requireNonNull(address, "AddressEmbeddable is required");

        return AddressData.builder()
                .street(address.getStreet())
                .number(address.getNumber())
                .complement(address.getComplement())
                .neighborhood(address.getNeighborhood())
                .city(address.getCity())
                .state(address.getState())
                .zipCode(address.getZipCode())
                .build();
    }

    private AddressData toAddressData(Address address) {
        Objects.requireNonNull(address, "Address is required");

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
