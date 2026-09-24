package com.henio.algashop.ordering.application.customer.query;

import com.henio.algashop.ordering.application.commons.AddressData;
import io.hypersistence.tsid.TSID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.OffsetDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CustomerOutput {

    private String id;
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
            Long id,
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
        this.id = TSID.from(id).toString();
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
