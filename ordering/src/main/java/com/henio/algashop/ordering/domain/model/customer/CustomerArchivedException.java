package com.henio.algashop.ordering.domain.model.customer;

import com.henio.algashop.ordering.domain.model.DomainException;

import static com.henio.algashop.ordering.domain.model.ErrorMessages.ERROR_CUSTOMER_ARCHIVED;

public class CustomerArchivedException extends DomainException {
    public CustomerArchivedException() {
        super(ERROR_CUSTOMER_ARCHIVED);
    }
}

