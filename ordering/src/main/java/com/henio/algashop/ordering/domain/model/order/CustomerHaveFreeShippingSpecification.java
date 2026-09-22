package com.henio.algashop.ordering.domain.model.order;

import com.henio.algashop.ordering.domain.model.customer.Customer;
import com.henio.algashop.ordering.domain.model.customer.LoyaltyPoints;
import com.henio.algashop.ordering.domain.model.shared.Specification;
import lombok.RequiredArgsConstructor;

import java.time.Year;

@RequiredArgsConstructor
public class CustomerHaveFreeShippingSpecification implements Specification<Customer> {

    private final Orders orders;

    private final int minPointsForFreeShippingRule1;
    private final long salesQuantityForFreeShippingRule1;

    private final int minPointsForFreeShippingRule2;

    @Override
    public boolean isSatisfiedBy(Customer customer) {
        return customer.loyaltyPoints().compareTo(new LoyaltyPoints(minPointsForFreeShippingRule1)) >= 0
                && orders.salesQuantityByCustomerInYear(customer.id(), Year.now()) >= salesQuantityForFreeShippingRule1
                || customer.loyaltyPoints().compareTo(new LoyaltyPoints(2000)) >= minPointsForFreeShippingRule2;
    }
}
