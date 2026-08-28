package com.henio.algashop.ordering.domain.model.valueobject;

import java.time.LocalDate;

public class RecipientTestDataBuilder {

    private RecipientTestDataBuilder() {}

    public static Recipient.RecipientBuilder aRecipient() {
        return Recipient.builder()
                .fullName(new FullName("John", "Doe"))
                .document(new Document("112-33-2321"))
                .phone(new Phone("111-441-1244"));
    }
}
