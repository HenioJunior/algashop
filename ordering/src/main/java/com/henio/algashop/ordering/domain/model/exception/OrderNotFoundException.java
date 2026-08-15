package com.henio.algashop.ordering.domain.model.exception;

import com.henio.algashop.ordering.domain.model.valueobject.id.OrderId;

import static com.henio.algashop.ordering.domain.model.exception.CustomerMessages.ERROR_ORDER_NOT_FOUND;

public class OrderNotFoundException extends DomainException{

    public OrderNotFoundException(OrderId orderId) {
        super(String.format(ERROR_ORDER_NOT_FOUND, orderId));
    }

}
