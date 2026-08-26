package com.henio.algashop.ordering.domain.service;


import com.henio.algashop.ordering.domain.model.entity.*;
import com.henio.algashop.ordering.domain.model.valueobject.LoyaltyPoints;
import lombok.RequiredArgsConstructor;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class CustomerLoyaltyPointsServiceTest {

    CustomerLoyaltyPointsService customerLoyaltyPointsService
            = new CustomerLoyaltyPointsService();
    @Test
    void givenValidCustomerAndOrder_WhenAddingPoints_ShouldAccumulate() {
        Customer customer = CustomerTestDataBuilder.existingCustomer().build();
        Order order = OrderTestDataBuilder.anOrder().status(OrderStatus.READY).build();

        customerLoyaltyPointsService.addPoints(customer, order);

        Assertions.assertThat(customer.loyaltyPoints()).isEqualTo(new LoyaltyPoints(30));

    }
}