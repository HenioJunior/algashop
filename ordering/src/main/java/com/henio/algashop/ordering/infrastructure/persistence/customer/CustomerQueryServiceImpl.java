package com.henio.algashop.ordering.infrastructure.persistence.customer;

import com.henio.algashop.ordering.application.customer.query.CustomerOutput;
import com.henio.algashop.ordering.application.customer.query.CustomerQueryService;
import com.henio.algashop.ordering.domain.model.customer.CustomerId;
import com.henio.algashop.ordering.domain.model.customer.exception.CustomerNotFoundException;
import io.hypersistence.tsid.TSID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CustomerQueryServiceImpl implements CustomerQueryService {

    private final CustomerPersistenceEntityRepository repository;

    @Override
    public CustomerOutput findById(String customerId) {
        long id = TSID.from(customerId).toLong();
        return repository.findByIdAsOutput(id).orElseThrow(
                () -> new CustomerNotFoundException(new CustomerId(TSID.from(customerId)))
        );
    }
}
