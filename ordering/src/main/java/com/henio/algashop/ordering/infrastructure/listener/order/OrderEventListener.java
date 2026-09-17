package com.henio.algashop.ordering.infrastructure.listener.order;

import com.henio.algashop.ordering.application.order.notification.*;
import com.henio.algashop.ordering.domain.model.order.OrderCanceledEvent;
import com.henio.algashop.ordering.domain.model.order.OrderPaidEvent;
import com.henio.algashop.ordering.domain.model.order.OrderPlacedEvent;
import com.henio.algashop.ordering.domain.model.order.OrderReadyEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class OrderEventListener {
    
    private final OrderNotificationService service;

    @EventListener
    public void handleOrderPlaced(OrderPlacedEvent event) {
        log.info("Order placed event received: {}", event);

        NotifyOrderPlacedInput input = new NotifyOrderPlacedInput(
                event.orderId().toString(),
                event.customerId().toString(),
                event.placedAt().toLocalDateTime()
        );

        log.info("Order placed notification sent: {}", input);

        service.notifyOrderPlaced(input);
    }

    @EventListener
    public void handleOrderPaid(OrderPaidEvent event) {
        log.info("Order paid event received: {}", event);

        NotifyOrderPaidInput input = new NotifyOrderPaidInput(
                event.orderId().toString(),
                event.customerId().toString(),
                event.paidAt().toLocalDateTime()
        );

        log.info("Order paid notification sent: {}", input);

        service.notifyOrderPaid(input);
    }

    @EventListener
    public void handleOrderReady(OrderReadyEvent event) {
        log.info("Order ready event received: {}", event);

        NotifyOrderReadyInput input = new NotifyOrderReadyInput(
                event.orderId().toString(),
                event.customerId().toString(),
                event.readyAt().toLocalDateTime()
        );

        log.info("Order ready notification sent: {}", input);

        service.notifyOrderReady(input);
    }

    @EventListener
    public void handleOrderCanceled(OrderCanceledEvent event) {
        log.info("Order canceled event received: {}", event);

        NotifyOrderCanceledInput input = new NotifyOrderCanceledInput(
                event.orderId().toString(),
                event.customerId().toString(),
                event.canceledAt().toLocalDateTime()
        );

        log.info("Order canceled notification sent: {}", input);

        service.notifyOrderCanceled(input);
    }
}
