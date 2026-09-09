package com.henio.algashop.ordering.domain.model.order;

import com.henio.algashop.ordering.domain.model.shared.DomainException;

import static com.henio.algashop.ordering.domain.model.validation.ErrorMessages.ERROR_ORDER_DELIVERY_DATE_CANNOT_BE_IN_THE_PAST;

public class OrderInvalidShippingDeliveryDateException extends DomainException {
    public OrderInvalidShippingDeliveryDateException(OrderId id) {
        super(String.format(ERROR_ORDER_DELIVERY_DATE_CANNOT_BE_IN_THE_PAST, id));
    }
}
