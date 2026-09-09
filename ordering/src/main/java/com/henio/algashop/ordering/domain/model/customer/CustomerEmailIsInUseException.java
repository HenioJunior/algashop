package com.henio.algashop.ordering.domain.model.customer;

import com.henio.algashop.ordering.domain.model.shared.DomainException;

import static com.henio.algashop.ordering.domain.model.validation.ErrorMessages.ERROR_EMAIL_ALREADY_REGISTERED;

public class CustomerEmailIsInUseException extends DomainException {

    public CustomerEmailIsInUseException() {
        super(ERROR_EMAIL_ALREADY_REGISTERED);
    }
}
