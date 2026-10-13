package com.henio.algashop.ordering.domain.model.customer.event;

import com.henio.algashop.ordering.domain.model.customer.CustomerId;

import java.time.OffsetDateTime;

public record CustomerArchivedEvent(
        CustomerId customerId,
        OffsetDateTime archivedAt
) {}
