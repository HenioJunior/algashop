package com.henio.algashop.ordering.domain.model.order;

import com.henio.algashop.ordering.domain.model.customer.CustomerId;

import java.time.OffsetDateTime;

public record OrderPaidEvent(
        OrderId orderId,
        CustomerId customerId,
        OffsetDateTime paidAt
) {}
