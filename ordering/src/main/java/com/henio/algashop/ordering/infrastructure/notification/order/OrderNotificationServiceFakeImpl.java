package com.henio.algashop.ordering.infrastructure.notification.order;

import com.henio.algashop.ordering.application.order.notification.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class OrderNotificationServiceFakeImpl implements OrderNotificationService {

    @Override
    public void notifyOrder(NotifyOrderPlacedInput input) {
        log.info("Order placed notification sent: {}", input);
    }

    @Override
    public void notifyOrder(NotifyOrderPaidInput input) {
        log.info("Order paid notification sent: {}", input);
    }

    @Override
    public void notifyOrder(NotifyOrderReadyInput input) {
        log.info("Order ready notification sent: {}", input);
    }

    @Override
    public void notifyOrder(NotifyOrderCanceledInput input) {
        log.info("Order canceled notification sent: {}", input);
    }
}
