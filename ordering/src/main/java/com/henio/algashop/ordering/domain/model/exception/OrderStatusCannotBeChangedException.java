package com.henio.algashop.ordering.domain.model.exception;

import com.henio.algashop.ordering.domain.model.entity.OrderStatus;
import com.henio.algashop.ordering.domain.model.valueobject.id.OrderId;

import static com.henio.algashop.ordering.domain.model.exception.CustomerMessages.ERROR_ORDER_STATUS_CANNOT_BE_CHANGED;

public class OrderStatusCannotBeChangedException extends DomainException {
    public OrderStatusCannotBeChangedException(OrderId orderId, OrderStatus currentStatus, OrderStatus newStatus) {
        super(String.format(ERROR_ORDER_STATUS_CANNOT_BE_CHANGED, orderId, currentStatus, newStatus));
    }
}
