package com.henio.algashop.ordering.infrastructure.persistence.disassembler;

import com.henio.algashop.ordering.domain.model.entity.Customer;
import com.henio.algashop.ordering.domain.model.valueobject.*;
import com.henio.algashop.ordering.domain.model.valueobject.id.CustomerId;
import com.henio.algashop.ordering.infrastructure.persistence.embeddable.AddressEmbeddable;
import com.henio.algashop.ordering.infrastructure.persistence.entity.CustomerPersistenceEntity;
import io.hypersistence.tsid.TSID;
import org.springframework.stereotype.Component;

@Component
public class CustomerPersistenceDisassembler {

public Customer toDomain(CustomerPersistenceEntity entity) {
	return Customer.existing()
            .id(new CustomerId(TSID.from(entity.getId())))
            .fullName(new FullName(entity.getFirstName(), entity.getLastName()))
            .birthDate(entity.getBirthDate() == null
                    ? null
                    : new BirthDate(entity.getBirthDate()))
            .email(entity.getEmail() == null
                    ? null
                    : new Email(entity.getEmail()))
            .phone(entity.getPhone() == null
                    ? null
                    : new Phone(entity.getPhone()))
            .document(entity.getDocument() == null
                    ? null
                    : new Document(entity.getDocument()))
            .promotionNotificationsAllowed(entity.isPromotionNotificationsAllowed())
            .archived(entity.isArchived())
            .registeredAt(entity.getRegisteredAt())
            .archivedAt(entity.getArchivedAt())
            .loyaltyPoints(new LoyaltyPoints(entity.getLoyaltyPoints()))
            .address(toAddress(entity.getAddress()))
            .version(entity.getVersion())
            .build();
}

    private static Address toAddress(AddressEmbeddable entity) {
        if (entity == null) {
            return null;
        }
        return Address.builder()
                .street(entity.getStreet())
                .complement(entity.getComplement())
                .neighborhood(entity.getNeighborhood())
                .number(entity.getNumber())
                .city(entity.getCity())
                .state(entity.getState())
                .zipCode(new ZipCode(entity.getZipCode()))
                .build();
    }
}
