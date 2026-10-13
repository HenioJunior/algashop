package com.henio.algashop.billing.domain.model.creditcard;

import com.henio.algashop.billing.shared.DomainException;

public class CreditCardNotFoundException extends DomainException {
    public CreditCardNotFoundException(String message) {
        super(message);
    }
}
