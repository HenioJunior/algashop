package com.henio.algashop.ordering.domain.model.customer.exception;

import com.henio.algashop.ordering.domain.model.shared.DomainException;

import static com.henio.algashop.ordering.domain.model.validation.ErrorMessages.ERROR_CUSTOMER_ARCHIVED;

public class CustomerArchivedException extends DomainException {
    public CustomerArchivedException() {
        super(ERROR_CUSTOMER_ARCHIVED);
    }
}

