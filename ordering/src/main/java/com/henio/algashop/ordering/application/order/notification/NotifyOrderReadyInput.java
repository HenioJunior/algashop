package com.henio.algashop.ordering.application.order.notification;

import java.time.OffsetDateTime;

public record NotifyOrderReadyInput(
        String orderId,
        String customerId,
        OffsetDateTime readyAt){
}
