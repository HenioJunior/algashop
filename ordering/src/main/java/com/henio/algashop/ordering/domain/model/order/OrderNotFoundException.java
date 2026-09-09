package com.henio.algashop.ordering.domain.model.order;

import com.henio.algashop.ordering.domain.model.shared.DomainException;

import static com.henio.algashop.ordering.domain.model.validation.ErrorMessages.ERROR_ORDER_NOT_FOUND;

public class OrderNotFoundException extends DomainException {

    public OrderNotFoundException(OrderId orderId) {
        super(String.format(ERROR_ORDER_NOT_FOUND, orderId));
    }

}
