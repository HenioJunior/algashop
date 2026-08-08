package com.henio.algashop.ordering.domain.model.exception;

import static com.henio.algashop.ordering.domain.model.exception.CustomerMessages.ERROR_CUSTOMER_ARCHIVED;

public class CustomerArchivedException extends DomainException {
    public CustomerArchivedException() {
        super(ERROR_CUSTOMER_ARCHIVED);
    }
}

