package com.henio.algashop.ordering.domain.model.exception;

import com.henio.algashop.ordering.domain.model.valueobject.id.CustomerId;

import static com.henio.algashop.ordering.domain.model.exception.CustomerMessages.ERROR_CUSTOMER_NOT_FOUND;

public class CustomerNotFoundException extends DomainException {

    public CustomerNotFoundException(CustomerId customerId) {
        super(String.format(ERROR_CUSTOMER_NOT_FOUND, customerId));
    }
}
