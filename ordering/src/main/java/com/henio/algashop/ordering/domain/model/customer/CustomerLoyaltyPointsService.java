package com.henio.algashop.ordering.domain.model.customer;

import com.henio.algashop.ordering.domain.model.order.Order;
import com.henio.algashop.ordering.domain.model.order.OrderNotBelongsToCustomerException;
import com.henio.algashop.ordering.domain.model.shared.DomainService;
import com.henio.algashop.ordering.domain.model.commons.Money;

import java.util.Objects;

@DomainService
public class CustomerLoyaltyPointsService {

    private static final LoyaltyPoints basePoints = new LoyaltyPoints(5);
    private static final Money expectedAmountToGivePoints = new Money("1000");

    public void addPoints(Customer customer, Order order) {
        Objects.requireNonNull(customer, "Customer is required");
        Objects.requireNonNull(order, "Order is required");

        if(!customer.id().equals(order.customerId())) {
            throw new OrderNotBelongsToCustomerException();
        }

        if(!order.isReady()) {
            throw new CantAddLoyaltyPointsOrderIsNotReady();
        }

        customer.addLoyaltyPoints(calculatePoints(order));

    }

    private LoyaltyPoints calculatePoints(Order order) {
        if (shouldGivePointsByAmount(order.totalAmount())) {
            Money result = order.totalAmount().divide(expectedAmountToGivePoints);
            return new LoyaltyPoints(result.value().intValue() * basePoints.value());
        }
        return LoyaltyPoints.ZERO;
    }

    private boolean shouldGivePointsByAmount(Money amount) {
        return amount.compareTo(expectedAmountToGivePoints) >= 0;
    }
}
