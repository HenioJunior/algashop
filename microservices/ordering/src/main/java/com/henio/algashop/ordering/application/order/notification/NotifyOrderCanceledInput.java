package com.henio.algashop.ordering.application.order.notification;

import java.time.OffsetDateTime;

public record NotifyOrderCanceledInput(
        String orderId,
        String customerId,
        OffsetDateTime canceledAt){
}
