package com.henio.algashop.ordering.domain.model.customer.exception;

import com.henio.algashop.ordering.domain.model.customer.CustomerId;
import com.henio.algashop.ordering.domain.model.shared.DomainException;

public class CustomerEmailAlreadyInUseException extends DomainException {
    public CustomerEmailAlreadyInUseException(CustomerId customerId) {
        super("Customer email already in use: " + customerId);
    }
}
