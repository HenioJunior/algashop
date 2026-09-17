package com.henio.algashop.ordering.application.order.notification;

import java.time.LocalDateTime;

public record NotifyOrderCanceledInput(
        String orderId,
        String customerId,
        LocalDateTime canceledAt){
}
