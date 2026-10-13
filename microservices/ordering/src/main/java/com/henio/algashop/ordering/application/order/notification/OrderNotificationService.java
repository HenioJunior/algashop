package com.henio.algashop.ordering.application.order.notification;

public interface OrderNotificationService {
    void notifyOrder(NotifyOrderPlacedInput input);
    void notifyOrder(NotifyOrderPaidInput input);
    void notifyOrder(NotifyOrderReadyInput input);
    void notifyOrder(NotifyOrderCanceledInput input);

}
