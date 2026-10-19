package com.henio.algashop.ordering.domain.model.shared;

public class DomainEntityNotFoundException extends RuntimeException{

    public DomainEntityNotFoundException(String message) {
        super(message);
    }
}
