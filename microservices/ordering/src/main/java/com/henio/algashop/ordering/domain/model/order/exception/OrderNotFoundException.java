package com.henio.algashop.ordering.domain.model.order.exception;

import com.henio.algashop.ordering.domain.model.order.OrderId;
import com.henio.algashop.ordering.domain.model.shared.DomainEntityNotFoundException;

import static com.henio.algashop.ordering.domain.model.validation.ErrorMessages.ERROR_ORDER_NOT_FOUND;

public class OrderNotFoundException extends DomainEntityNotFoundException {

    public OrderNotFoundException(OrderId orderId) {
        super(String.format(ERROR_ORDER_NOT_FOUND, orderId));
    }

}
