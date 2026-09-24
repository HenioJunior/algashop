package com.henio.algashop.ordering.infrastructure.persistence.customer;

import com.henio.algashop.ordering.application.customer.query.CustomerOutput;

import java.util.Optional;

public interface CustomerPersistenceEntityQueries {
    Optional<CustomerOutput> findByIdAsOutput(long id);
}
