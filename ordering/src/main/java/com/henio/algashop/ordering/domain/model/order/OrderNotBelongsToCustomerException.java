package com.henio.algashop.ordering.domain.model.order;

import com.henio.algashop.ordering.domain.model.DomainException;

import static com.henio.algashop.ordering.domain.model.ErrorMessages.ERROR_CUSTOMER_AND_ORDER_MUST_BELONG_TO_THE_SAME_CUSTOMER;

public class OrderNotBelongsToCustomerException extends DomainException {

    public OrderNotBelongsToCustomerException() {
        super(ERROR_CUSTOMER_AND_ORDER_MUST_BELONG_TO_THE_SAME_CUSTOMER);
    }
}
