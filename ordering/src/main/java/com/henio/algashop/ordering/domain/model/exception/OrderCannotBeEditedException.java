package com.henio.algashop.ordering.domain.model.exception;

import com.henio.algashop.ordering.domain.model.entity.OrderStatus;
import com.henio.algashop.ordering.domain.model.valueobject.id.OrderId;

import static com.henio.algashop.ordering.domain.model.exception.CustomerMessages.ERROR_ORDER_CANNOT_BE_EDITED;

public class OrderCannotBeEditedException extends DomainException {
    public OrderCannotBeEditedException(OrderId id, OrderStatus status) {
        super(String.format(ERROR_ORDER_CANNOT_BE_EDITED, id, status));
    }
}
