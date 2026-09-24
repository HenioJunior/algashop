package com.henio.algashop.ordering.application.customer.query;

public interface CustomerQueryService {
    CustomerOutput findById(String customerId);
}
