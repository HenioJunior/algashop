package com.henio.algashop.billing.domain.model.invoice;

import com.henio.algashop.billing.shared.DomainException;

public class InvoiceNotFoundException extends DomainException {
    public InvoiceNotFoundException(String message) {
        super(message);
    }
}
