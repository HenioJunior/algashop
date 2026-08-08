package com.henio.algashop.ordering.domain.model.valueobject;

import com.henio.algashop.ordering.domain.model.validation.FieldValidator;

public record ProductName(String value) {

    public ProductName {
        FieldValidator.requireNonBlank(value);
    }

    @Override
    public String toString() {
        return value;
    }

}
