package com.henio.algashop.ordering.application.customer.query;

import org.springframework.data.domain.Page;

public interface CustomerQueryService {
    CustomerOutput findById(String customerId);
    Page<CustomerSummaryOutput> filter(CustomerFilter filter);
}
