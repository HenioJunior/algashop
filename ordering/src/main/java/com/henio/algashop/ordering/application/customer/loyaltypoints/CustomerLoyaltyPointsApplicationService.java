package com.henio.algashop.ordering.application.customer.loyaltypoints;

import com.henio.algashop.ordering.domain.model.customer.*;
import com.henio.algashop.ordering.domain.model.order.Order;
import com.henio.algashop.ordering.domain.model.order.OrderId;
import com.henio.algashop.ordering.domain.model.order.OrderNotFoundException;
import com.henio.algashop.ordering.domain.model.order.Orders;
import io.hypersistence.tsid.TSID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomerLoyaltyPointsApplicationService {

    private final Customers customers;
    private final Orders orders;

    private final CustomerLoyaltyPointsService customerLoyaltyPointsService;

    public void addLoyaltyPoints(String rawCustomerId, String rawOrderId) {
        Objects.requireNonNull(rawCustomerId, "rawCustomerId must not be null");
        Objects.requireNonNull(rawOrderId, "rawOrderId must not be null");

        Customer customer = customers.ofId(new CustomerId(TSID.from(rawCustomerId)))
                .orElseThrow(() -> new CustomerNotFoundException(new CustomerId(TSID.from(rawCustomerId))));

        Order order = orders.ofId(new OrderId(TSID.from(rawOrderId)))
                .orElseThrow(() -> new OrderNotFoundException(new OrderId(TSID.from(rawOrderId))));

        customerLoyaltyPointsService.addPoints(customer, order);

        customers.add(customer);
    }
}
