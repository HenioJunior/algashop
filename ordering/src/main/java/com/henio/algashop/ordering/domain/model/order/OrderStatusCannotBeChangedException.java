package com.henio.algashop.ordering.domain.model.order;

import com.henio.algashop.ordering.domain.model.shared.DomainException;

import static com.henio.algashop.ordering.domain.model.validation.ErrorMessages.ERROR_ORDER_STATUS_CANNOT_BE_CHANGED;

public class OrderStatusCannotBeChangedException extends DomainException {
    public OrderStatusCannotBeChangedException(OrderId orderId, OrderStatus currentStatus, OrderStatus newStatus) {
        super(String.format(ERROR_ORDER_STATUS_CANNOT_BE_CHANGED, orderId, currentStatus, newStatus));
    }
}
