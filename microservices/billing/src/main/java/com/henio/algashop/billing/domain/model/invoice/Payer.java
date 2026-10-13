package com.henio.algashop.billing.domain.model.invoice;

import com.henio.algashop.billing.domain.model.FieldValidations;
import jakarta.persistence.*;
import lombok.*;

import java.util.Objects;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Data
@EqualsAndHashCode
@Builder
@Embeddable
public class Payer {
    private String fullName;
    private String document;
    private String phone;
    private String email;

    @Embedded
    private Address address;

    private Payer(String fullName, String document, String phone, String email, Address address) {
        FieldValidations.requiresNonBlank(fullName);
        FieldValidations.requiresNonBlank(document);
        FieldValidations.requiresNonBlank(phone);
        FieldValidations.requiresValidEmail(email);
        Objects.requireNonNull(address, "Address cannot be null");

        this.fullName = fullName;
        this.document = document;
        this.phone = phone;
        this.email = email;
        this.address = address;
    }
}
