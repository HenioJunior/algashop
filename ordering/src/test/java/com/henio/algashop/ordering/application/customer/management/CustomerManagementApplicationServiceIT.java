package com.henio.algashop.ordering.application.customer.management;

import com.henio.algashop.ordering.application.commons.AddressData;
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

    @Test
    void shouldUpdateCustomer() {
        CustomerInput customerInput = CustomerInputTestDataBuilder.aCustomer().build();
        CustomerUpdateInput customerUpdate = CustomerUpdateInputTestDataBuilder.aCustomerUpdate().build();

        TSID customerId = customerManagementApplicationService.create(customerInput);
        assertThat(customerId).isNotNull();

        customerManagementApplicationService.update(customerId.toString(), customerUpdate);

        CustomerOutput customerOutput = customerManagementApplicationService.findById(new CustomerId(customerId));

        assertThat(customerOutput)
                .extracting(
                        CustomerOutput::getId,
                        CustomerOutput::getFirstName,
                        CustomerOutput::getLastName,
                        CustomerOutput::getEmail,
                        CustomerOutput::getBirthDate
                ).containsExactly(
                        customerId.toString(),
                        "Matt",
                        "Damon",
                        "johndoe@email.com",
                        LocalDate.of(1991, 7,5)
                );

        assertThat(customerOutput.getRegisteredAt()).isNotNull();
    }
}