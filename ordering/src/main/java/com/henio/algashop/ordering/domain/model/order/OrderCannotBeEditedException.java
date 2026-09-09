package com.henio.algashop.ordering.domain.model.order;

import com.henio.algashop.ordering.domain.model.DomainException;

import static com.henio.algashop.ordering.domain.model.ErrorMessages.ERROR_ORDER_CANNOT_BE_EDITED;

public class OrderCannotBeEditedException extends DomainException {
    public OrderCannotBeEditedException(OrderId id, OrderStatus status) {
        super(String.format(ERROR_ORDER_CANNOT_BE_EDITED, id, status));
    }
}
