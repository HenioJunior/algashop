package com.henio.algashop.ordering.application.customer.management;

import com.henio.algashop.ordering.domain.model.customer.CustomerAlreadyArchivedException;
import com.henio.algashop.ordering.domain.model.customer.CustomerId;
import com.henio.algashop.ordering.domain.model.customer.CustomerNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

import static com.henio.algashop.ordering.domain.model.customer.CustomerTestDataBuilder.DEFAULT_CUSTOMER_ID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Transactional
class CustomerManagementApplicationServiceIT {

    @Autowired
    private CustomerManagementApplicationService customerManagementApplicationService;

    @Test
    void shouldGenerateNewCustomer() {
        CustomerInput input = CustomerInputTestDataBuilder.aCustomer().build();

        CustomerId customerId = customerManagementApplicationService.create(input);
        assertThat(customerId).isNotNull();

        CustomerOutput customerOutput = customerManagementApplicationService
                .findById(customerId);

        assertThat(customerOutput).extracting(
                CustomerOutput::getId,
                CustomerOutput::getFirstName,
                CustomerOutput::getLastName,
                CustomerOutput::getEmail,
                CustomerOutput::getBirthDate
        ).contains(
                customerId.toString(),
                "John",
                "Doe",
                "johndoe@email.com",
                LocalDate.of(1991, 7,5)
        );

        assertThat(customerOutput.getRegisteredAt()).isNotNull();
    }

    @Test
    void shouldUpdateCustomer() {
        CustomerInput customerInput = CustomerInputTestDataBuilder.aCustomer().email("johndoe2@email.com").build();
        CustomerUpdateInput customerUpdate = CustomerUpdateInputTestDataBuilder.aCustomerUpdate().build();

        CustomerId customerId = customerManagementApplicationService.create(customerInput);
        assertThat(customerId).isNotNull();

        customerManagementApplicationService.update(customerId.toString(), customerUpdate);

        CustomerOutput customerOutput = customerManagementApplicationService.findById(customerId);

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
                        "matt.damon@email.com",
                        LocalDate.of(1991, 7,5)
                );

        assertThat(customerOutput.getRegisteredAt()).isNotNull();
    }

    @Test
    void shouldArchiveCustomer() {
        CustomerInput customerInput = CustomerInputTestDataBuilder.aCustomer().build();

        CustomerId customerId = customerManagementApplicationService.create(customerInput);

        customerManagementApplicationService.archive(customerId.toString());

        CustomerOutput customerOutput =
                customerManagementApplicationService.findById(customerId);

        assertThat(customerOutput).isNotNull();
        assertThat(customerOutput.getArchived()).isTrue();
        assertThat(customerOutput.getArchivedAt()).isNotNull();

        assertThat(customerOutput.getFirstName()).isEqualTo("Anonymous");
        assertThat(customerOutput.getLastName()).isEqualTo("Customer");

        assertThat(customerOutput.getBirthDate()).isNull();
        assertThat(customerOutput.getPhone()).isNull();
        assertThat(customerOutput.getDocument()).isNull();

        assertThat(customerOutput.getEmail())
                .endsWith("@anonymous.invalid");

        assertThat(customerOutput.getPromotionNotificationsAllowed())
                .isFalse();
    }

    @Test
    void shouldThrowExceptionWhenArchivingNonExistingCustomer() {
        assertThatThrownBy(() -> customerManagementApplicationService.archive(DEFAULT_CUSTOMER_ID.toString()))
                .isInstanceOf(CustomerNotFoundException.class);
    }

    @Test
    void shouldThrowExceptionWhenArchivingExistingCustomerArchived() {
        CustomerInput customerInput = CustomerInputTestDataBuilder.aCustomer().build();

        CustomerId customerId = customerManagementApplicationService.create(customerInput);

        customerManagementApplicationService.archive(customerId.toString());

        CustomerOutput customerOutput =
                customerManagementApplicationService.findById(customerId);

        assertThat(customerOutput.getArchived()).isTrue();

        assertThatThrownBy(() -> customerManagementApplicationService.archive(customerId.toString()))
                .isInstanceOf(CustomerAlreadyArchivedException.class);
    }
}