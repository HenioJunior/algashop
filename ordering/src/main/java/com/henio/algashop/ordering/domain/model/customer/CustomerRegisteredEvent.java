package com.henio.algashop.ordering.domain.model.customer;

import com.henio.algashop.ordering.domain.model.commons.Email;
import com.henio.algashop.ordering.domain.model.commons.FullName;

import java.time.OffsetDateTime;

public record CustomerRegisteredEvent(
        CustomerId customerId,
        OffsetDateTime registeredA,
        FullName fullName,
        Email email
) {}
