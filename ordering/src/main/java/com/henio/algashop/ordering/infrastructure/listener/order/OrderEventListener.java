package com.henio.algashop.ordering.infrastructure.listener.order;

import com.henio.algashop.ordering.application.order.notification.*;
import com.henio.algashop.ordering.domain.model.order.event.OrderCanceledEvent;
import com.henio.algashop.ordering.domain.model.order.event.OrderPaidEvent;
import com.henio.algashop.ordering.domain.model.order.event.OrderPlacedEvent;
import com.henio.algashop.ordering.domain.model.order.event.OrderReadyEvent;
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
    public void handleOrder(OrderPlacedEvent event) {
        log.info("Order placed event received: {}", event);

        NotifyOrderPlacedInput input = new NotifyOrderPlacedInput(
                event.orderId().toString(),
                event.customerId().toString(),
                event.placedAt().toLocalDateTime()
        );

        service.notifyOrder(input);
    }

    @EventListener
    public void handleOrder(OrderPaidEvent event) {
        log.info("Order paid event received: {}", event);

        NotifyOrderPaidInput input = new NotifyOrderPaidInput(
                event.orderId().toString(),
                event.customerId().toString(),
                event.paidAt().toLocalDateTime()
        );

        service.notifyOrder(input);
    }

    @EventListener
    public void handleOrder(OrderReadyEvent event) {
        log.info("Order ready event received: {}", event);

        NotifyOrderReadyInput input = new NotifyOrderReadyInput(
                event.orderId().toString(),
                event.customerId().toString(),
                event.readyAt().toLocalDateTime()
        );

        service.notifyOrder(input);
    }

    @EventListener
    public void handleOrder(OrderCanceledEvent event) {
        log.info("Order canceled event received: {}", event);

        NotifyOrderCanceledInput input = new NotifyOrderCanceledInput(
                event.orderId().toString(),
                event.customerId().toString(),
                event.canceledAt().toLocalDateTime()
        );

        service.notifyOrder(input);
    }
}
