package com.henio.algashop.ordering.domain.model.customer.event;

import com.henio.algashop.ordering.domain.model.commons.Email;
import com.henio.algashop.ordering.domain.model.commons.FullName;
import com.henio.algashop.ordering.domain.model.customer.CustomerId;

import java.time.OffsetDateTime;

public record CustomerRegisteredEvent(
        CustomerId customerId,
        OffsetDateTime registeredA,
        FullName fullName,
        Email email
) {}
