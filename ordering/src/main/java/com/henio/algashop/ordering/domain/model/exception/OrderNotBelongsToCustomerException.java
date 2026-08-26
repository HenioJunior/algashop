package com.henio.algashop.ordering.domain.model.exception;

import static com.henio.algashop.ordering.domain.model.exception.CustomerMessages.ERROR_CUSTOMER_AND_ORDER_MUST_BELONG_TO_THE_SAME_CUSTOMER;

public class OrderNotBelongsToCustomerException extends DomainException{

    public OrderNotBelongsToCustomerException() {
        super(ERROR_CUSTOMER_AND_ORDER_MUST_BELONG_TO_THE_SAME_CUSTOMER);
    }
}
