package com.henio.algashop.ordering.application.order.notification;

import java.time.LocalDateTime;

public record NotifyOrderPaidInput(
        String orderId,
        String customerId,
        LocalDateTime paidAt){
}
