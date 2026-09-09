package com.henio.algashop.ordering.domain.model.customer;

import com.henio.algashop.ordering.domain.model.shared.DomainException;

import static com.henio.algashop.ordering.domain.model.validation.ErrorMessages.ERROR_CUSTOMER_NOT_FOUND;

public class CustomerNotFoundException extends DomainException {

    public CustomerNotFoundException(CustomerId customerId) {
        super(String.format(ERROR_CUSTOMER_NOT_FOUND, customerId));
    }
}
