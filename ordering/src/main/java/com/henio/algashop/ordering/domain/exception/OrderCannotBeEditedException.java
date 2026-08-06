package com.henio.algashop.ordering.domain.exception;

import com.henio.algashop.ordering.domain.entity.OrderStatus;
import com.henio.algashop.ordering.domain.valueobject.id.OrderId;

import static com.henio.algashop.ordering.domain.exception.CustomerMessages.ERROR_ORDER_CANNOT_BE_EDITED;

public class OrderCannotBeEditedException extends DomainException {

    public OrderCannotBeEditedException(OrderId id, OrderStatus status) {
        super(String.format(ERROR_ORDER_CANNOT_BE_EDITED, id, status));
    }
}
