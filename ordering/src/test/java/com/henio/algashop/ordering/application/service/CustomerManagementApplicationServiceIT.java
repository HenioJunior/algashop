package com.henio.algashop.ordering.application.service;

import com.henio.algashop.ordering.application.model.AddressData;
import com.henio.algashop.ordering.application.model.CustomerInput;
import com.henio.algashop.ordering.application.model.CustomerOutput;
import com.henio.algashop.ordering.domain.model.customer.CustomerId;
import io.hypersistence.tsid.TSID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class CustomerManagementApplicationServiceIT {

    @Autowired
    private CustomerManagementApplicationService customerManagementApplicationService;

    @Test
    void shouldGenerateNewCustomer() {
        CustomerInput input = CustomerInput.builder()
                .firstName("John")
                .lastName("Doe")
                .birthDate(LocalDate.of(1991, 7,5))
                .document("255-08-0578")
                .phone("478-256-2604")
                .email("johndoe@email.com")
                .promotionNotificationsAllowed(false)
                .address(AddressData.builder()
                        .street("Bourbon Street")
                        .number("1200")
                        .complement("Apt. 901")
                        .neighborhood("North Ville")
                        .city("Yostfort")
                        .state("South Carolina")
                        .zipCode("70283")
                        .build())
                .build();

        TSID customerId = customerManagementApplicationService.create(input);
        assertThat(customerId).isNotNull();

        CustomerOutput customerOutput = customerManagementApplicationService.findById(new CustomerId(customerId));

        assertThat(customerOutput.getId()).isEqualTo(customerId.toString());
        assertThat(customerOutput.getFirstName()).isEqualTo("John");
        assertThat(customerOutput.getLastName()).isEqualTo("Doe");
        assertThat(customerOutput.getEmail()).isEqualTo("johndoe@email.com");
        assertThat(customerOutput.getBirthDate()).isEqualTo(LocalDate.of(1991, 7,5));
        assertThat(customerOutput.getRegisteredAt()).isNotNull();

    }
}