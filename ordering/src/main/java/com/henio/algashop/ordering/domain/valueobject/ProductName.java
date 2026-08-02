package com.henio.algashop.ordering.domain.valueobject;

import com.henio.algashop.ordering.domain.validation.FieldValidator;

public record ProductName(String value) {

    public ProductName {
        FieldValidator.requireNonBlank(value);
    }

    @Override
    public String toString() {
        return value;
    }

}
