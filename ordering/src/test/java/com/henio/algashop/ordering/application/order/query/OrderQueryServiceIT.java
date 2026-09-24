package com.henio.algashop.ordering.application.order.query;

import com.henio.algashop.ordering.domain.model.customer.Customer;
import com.henio.algashop.ordering.domain.model.customer.CustomerTestDataBuilder;
import com.henio.algashop.ordering.domain.model.customer.Customers;
import com.henio.algashop.ordering.domain.model.order.Order;
import com.henio.algashop.ordering.domain.model.order.OrderTestDataBuilder;
import com.henio.algashop.ordering.domain.model.order.Orders;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class OrderQueryServiceIT {

    @Autowired
    private OrderQueryService orderQueryService;

    @Autowired
    private Orders orders;

    @Autowired
    private Customers customers;

    @Test
    void shouldReturnOrderById() {
        Customer customer = CustomerTestDataBuilder.existingCustomer().build();
        customers.add(customer);

        Order order = OrderTestDataBuilder.anOrder().customerId(customer.id()).build();
        orders.add(order);

        OrderDetailOutput output = orderQueryService.findById(order.id().toString());

        assertThat(output)
                .extracting(
                        OrderDetailOutput::getId,
                        OrderDetailOutput::getTotalAmount
                ).containsExactly(
                        order.id().toString(),
                        order.totalAmount().value()
                );

    }
}