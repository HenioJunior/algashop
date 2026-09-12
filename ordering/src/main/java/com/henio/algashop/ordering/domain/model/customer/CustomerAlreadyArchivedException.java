package com.henio.algashop.ordering.domain.model.customer;

import com.henio.algashop.ordering.domain.model.shared.DomainException;

public class CustomerAlreadyArchivedException extends DomainException {

    public CustomerAlreadyArchivedException(CustomerId customerId) {
        super("Customer with id " + customerId + " is already archived");
    }
}
