package com.henio.algashop.ordering.infrastructure.beans;

import com.henio.algashop.ordering.domain.model.order.CustomerHaveFreeShippingSpecification;
import com.henio.algashop.ordering.domain.model.order.Orders;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpecificationBeansConfig {

    @Bean
    public CustomerHaveFreeShippingSpecification customerHaveFreeShippingSpecification(Orders orders) {
        return new CustomerHaveFreeShippingSpecification(
                orders,
                100,
                2,
                2000
        );
    }
}
