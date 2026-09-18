package com.henio.algashop.ordering.domain.model.order.event;

import com.henio.algashop.ordering.domain.model.customer.CustomerId;
import com.henio.algashop.ordering.domain.model.order.OrderId;

import java.time.OffsetDateTime;

public record OrderReadyEvent(
        OrderId orderId,
        CustomerId customerId,
        OffsetDateTime readyAt
) {}
