package com.henio.algashop.ordering.infrastructure.notification.order;

import com.henio.algashop.ordering.application.order.notification.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class OrderNotificationServiceFakeImpl implements OrderNotificationService {

    @Override
    public void notifyOrderPlaced(NotifyOrderPlacedInput input) {
        log.info("Order placed notification sent: {}", input);

    }

    @Override
    public void notifyOrderPaid(NotifyOrderPaidInput input) {
        log.info("Order paid notification sent: {}", input);
    }

    @Override
    public void notifyOrderReady(NotifyOrderReadyInput input) {
        log.info("Order ready notification sent: {}", input);
    }

    @Override
    public void notifyOrderCanceled(NotifyOrderCanceledInput input) {
        log.info("Order canceled notification sent: {}", input);
    }
}
