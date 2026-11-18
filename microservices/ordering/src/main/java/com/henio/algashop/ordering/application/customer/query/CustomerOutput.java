package com.henio.algashop.ordering.application.customer.query;

import com.henio.algashop.ordering.application.commons.AddressData;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CustomerOutput {

    private UUID id;
    private String firstName;
    private String lastName;
    private String email;
    private String document;
    private String phone;
    private LocalDate birthDate;
    private Integer loyaltyPoints;
    private OffsetDateTime registeredAt;
    private OffsetDateTime archivedAt;
    private Boolean promotionNotificationsAllowed;
    private Boolean archived;
    private AddressData address;

    public CustomerOutput(
            UUID id,
            String firstName,
            String lastName,
            String email,
            String document,
            String phone,
            LocalDate birthDate,
            Integer loyaltyPoints,
            OffsetDateTime registeredAt,
            OffsetDateTime archivedAt,
            boolean promotionNotificationsAllowed,
            boolean archived,
            AddressData address
    ) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.document = document;
        this.phone = phone;
        this.birthDate = birthDate;
        this.loyaltyPoints = loyaltyPoints;
        this.registeredAt = registeredAt;
        this.archivedAt = archivedAt;
        this.promotionNotificationsAllowed = promotionNotificationsAllowed;
        this.archived = archived;
        this.address = address;
    }

}
