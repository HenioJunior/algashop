package com.henio.algashop.ordering.domain.model.order.shipping;

import com.henio.algashop.ordering.domain.model.commons.Document;
import com.henio.algashop.ordering.domain.model.commons.FullName;
import com.henio.algashop.ordering.domain.model.commons.Phone;
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
