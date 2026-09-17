package com.henio.algashop.ordering.application.order.notification;

public interface OrderNotificationService {
    void notifyOrderPlaced(NotifyOrderPlacedInput input);
    void notifyOrderPaid(NotifyOrderPaidInput input);
    void notifyOrderReady(NotifyOrderReadyInput input);
    void notifyOrderCanceled(NotifyOrderCanceledInput input);

}
