package com.henio.algashop.ordering.infrastructure.notification.customer;

import com.henio.algashop.ordering.application.customer.notification.CustomerNotificationService;
import com.henio.algashop.ordering.domain.model.customer.Customer;
import com.henio.algashop.ordering.domain.model.customer.CustomerId;
import com.henio.algashop.ordering.domain.model.customer.CustomerNotFoundException;
import com.henio.algashop.ordering.domain.model.customer.Customers;
import io.hypersistence.tsid.TSID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomerNotificationServiceFakeImpl implements CustomerNotificationService {

    private final Customers customers;

    @Override
    public void notifyNewRegistration(String rawCustomerId) {
        CustomerId customerId = new CustomerId(TSID.from(rawCustomerId));
        Customer customer = customers.ofId(customerId)
                .orElseThrow(() -> new CustomerNotFoundException(customerId));

        log.info("Welcome {}", customer.fullName().firstName());
        log.info("Use your email to access your account {}", customer.email());
    }
}
