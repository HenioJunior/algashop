package com.henio.algashop.ordering.application.order.notification;

import java.time.OffsetDateTime;

public record NotifyOrderPlacedInput(
        String orderId,
        String customerId,
        OffsetDateTime placedAt){
}
