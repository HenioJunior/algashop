package com.henio.algashop.ordering.domain.model.valueobject;

import lombok.Builder;

import java.util.Objects;

@Builder
public record Recipient(FullName fullName, Document document, Phone phone) {

    public Recipient {
     Objects.requireNonNull(fullName, "Recipient full name is required");
     Objects.requireNonNull(document, "Recipient document is required");
     Objects.requireNonNull(phone, "Recipient phone is required");
    }
}
