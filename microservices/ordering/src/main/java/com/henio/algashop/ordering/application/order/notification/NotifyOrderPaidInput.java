package com.henio.algashop.ordering.application.order.notification;

import java.time.OffsetDateTime;

public record NotifyOrderPaidInput(
        String orderId,
        String customerId,
        OffsetDateTime paidAt){
}
