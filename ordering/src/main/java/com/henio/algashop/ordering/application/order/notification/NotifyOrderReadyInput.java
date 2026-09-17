package com.henio.algashop.ordering.application.order.notification;

import java.time.LocalDateTime;

public record NotifyOrderReadyInput(
        String orderId,
        String customerId,
        LocalDateTime readyAt){
}
