package com.henio.algashop.ordering.domain.model.exception;

import static com.henio.algashop.ordering.domain.model.exception.CustomerMessages.ERROR_EMAIL_ALREADY_REGISTERED;

public class CustomerEmailIsInUseException extends DomainException{

    public CustomerEmailIsInUseException() {
        super(ERROR_EMAIL_ALREADY_REGISTERED);
    }
}
