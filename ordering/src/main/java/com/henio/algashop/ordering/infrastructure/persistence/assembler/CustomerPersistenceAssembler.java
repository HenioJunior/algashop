package com.henio.algashop.ordering.infrastructure.persistence.assembler;

import com.henio.algashop.ordering.domain.model.entity.Customer;
import com.henio.algashop.ordering.domain.model.valueobject.Address;
import com.henio.algashop.ordering.infrastructure.persistence.embeddable.AddressEmbeddable;
import com.henio.algashop.ordering.infrastructure.persistence.entity.CustomerPersistenceEntity;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class CustomerPersistenceAssembler {

    public CustomerPersistenceEntity fromDomain(Customer customer) {
        Objects.requireNonNull(customer, "Customer is required");

        return CustomerPersistenceEntity.builder()
                .id(customer.id().value().toLong())
                .firstName(customer.fullName().firstName())
                .lastName(customer.fullName().lastName())
                .birthDate(customer.birthDate() == null ? null : customer.birthDate().value())
                .email(customer.email() == null ? null : customer.email().toString())
                .phone(customer.phone() == null ? null : customer.phone().toString())
                .document(customer.document() == null ? null : customer.document().toString())
                .promotionNotificationsAllowed(customer.isPromotionNotificationsAllowed())
                .archived(customer.isArchived())
                .registeredAt(customer.registeredAt())
                .archivedAt(customer.archivedAt())
                .loyaltyPoints(customer.loyaltyPoints().value())
                .address(toAddressEmbeddable(customer.address()))
                .version(customer.version())
                .build();
    }

    public CustomerPersistenceEntity merge(CustomerPersistenceEntity entity, Customer customer) {
        Objects.requireNonNull(entity, "Customer persistence entity is required");
        Objects.requireNonNull(customer, "Customer is required");

        entity.setId(customer.id().value().toLong());
        entity.setFirstName(customer.fullName().firstName());
        entity.setLastName(customer.fullName().lastName());
        entity.setBirthDate(customer.birthDate() == null ? null : customer.birthDate().value());
        entity.setEmail(customer.email() == null ? null : customer.email().toString());
        entity.setPhone(customer.phone() == null ? null : customer.phone().toString());
        entity.setDocument(customer.document() == null ? null : customer.document().toString());
        entity.setPromotionNotificationsAllowed(customer.isPromotionNotificationsAllowed());
        entity.setArchived(customer.isArchived());
        entity.setRegisteredAt(customer.registeredAt());
        entity.setArchivedAt(customer.archivedAt());
        entity.setLoyaltyPoints(customer.loyaltyPoints().value());
        entity.setAddress(toAddressEmbeddable(customer.address()));

        return entity;
    }

    private AddressEmbeddable toAddressEmbeddable(Address address) {
        if (address == null) {
            return null;
        }
        return AddressEmbeddable.builder()
                .city(address.city())
                .state(address.state())
                .number(address.number())
                .street(address.street())
                .complement(address.complement())
                .neighborhood(address.neighborhood())
                .zipCode(address.zipCode().value())
                .build();
    }
}
