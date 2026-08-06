package com.henio.algashop.ordering.domain.exception;

import static com.henio.algashop.ordering.domain.exception.CustomerMessages.ERROR_CUSTOMER_ARCHIVED;

public class CustomerArchivedException extends DomainException {
    public CustomerArchivedException() {
        super(ERROR_CUSTOMER_ARCHIVED);
    }
}

