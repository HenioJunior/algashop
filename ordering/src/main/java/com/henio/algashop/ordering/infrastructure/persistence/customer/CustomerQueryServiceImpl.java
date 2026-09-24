package com.henio.algashop.ordering.infrastructure.persistence.customer;

import com.henio.algashop.ordering.application.customer.query.CustomerOutput;
import com.henio.algashop.ordering.application.customer.management.CustomerOutputMapper;
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
    private final CustomerOutputMapper mapper;

    @Override
    public CustomerOutput findById(String customerId) {
        CustomerId id = new CustomerId(TSID.from(customerId));

        CustomerPersistenceEntity entity = repository
                .findById(id.value().toLong())
                .orElseThrow(() -> new CustomerNotFoundException(id));

        return mapper.fromPersistence(entity);
    }
}
