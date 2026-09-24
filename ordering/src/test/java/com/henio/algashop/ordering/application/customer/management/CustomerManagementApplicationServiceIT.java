package com.henio.algashop.ordering.application.customer.management;

import com.henio.algashop.ordering.application.customer.notification.CustomerNotificationApplicationService;
import com.henio.algashop.ordering.application.customer.notification.NotifyNewRegistrationInput;
import com.henio.algashop.ordering.application.customer.query.CustomerOutput;
import com.henio.algashop.ordering.application.customer.query.CustomerQueryService;
import com.henio.algashop.ordering.domain.model.customer.*;
import com.henio.algashop.ordering.domain.model.customer.event.CustomerRegisteredEvent;
import com.henio.algashop.ordering.domain.model.customer.exception.CustomerAlreadyArchivedException;
import com.henio.algashop.ordering.domain.model.customer.exception.CustomerEmailIsInUseException;
import com.henio.algashop.ordering.domain.model.customer.exception.CustomerNotFoundException;
import com.henio.algashop.ordering.domain.model.shared.DomainException;
import com.henio.algashop.ordering.infrastructure.listener.customer.CustomerEventListener;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

import static com.henio.algashop.ordering.domain.model.customer.CustomerTestDataBuilder.DEFAULT_CUSTOMER_ID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Transactional
class CustomerManagementApplicationServiceIT {

    @MockitoSpyBean
    private CustomerEventListener customerEventListener;

    @MockitoSpyBean
    private CustomerNotificationApplicationService customerNotificationApplicationService;

    @Autowired
    private CustomerManagementApplicationService customerManagementApplicationService;

    @Autowired
    private CustomerQueryService customerQueryService;

    @Test
    void shouldGenerateNewCustomer() {
        CustomerInput input = CustomerInputTestDataBuilder.aCustomer().build();

        CustomerId customerId = customerManagementApplicationService.create(input);
        assertThat(customerId).isNotNull();

        CustomerOutput customerOutput = customerQueryService.findById(customerId.toString());

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

        Mockito.verify(customerEventListener)
                .listen(Mockito.any(CustomerRegisteredEvent.class));

        Mockito.verify(customerNotificationApplicationService)
                .notifyNewRegistration(Mockito.any(NotifyNewRegistrationInput.class));
    }

    @Test
    void shouldUpdateCustomer() {
        CustomerInput customerInput = CustomerInputTestDataBuilder.aCustomer().email("johndoe2@email.com").build();
        CustomerUpdateInput customerUpdate = CustomerUpdateInputTestDataBuilder.aCustomerUpdate().build();

        CustomerId customerId = customerManagementApplicationService.create(customerInput);
        assertThat(customerId).isNotNull();

        customerManagementApplicationService.update(customerId.toString(), customerUpdate);

        CustomerOutput customerOutput = customerQueryService.findById(customerId.toString());

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
                customerQueryService.findById(customerId.toString());

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
    void shouldThrowExceptionWhenArchivingAlreadyArchivedCustomer() {
        CustomerInput customerInput = CustomerInputTestDataBuilder.aCustomer().build();

        CustomerId customerId = customerManagementApplicationService.create(customerInput);

        customerManagementApplicationService.archive(customerId.toString());

        CustomerOutput customerOutput =
                customerQueryService.findById(customerId.toString());

        assertThat(customerOutput.getArchived()).isTrue();

        assertThatThrownBy(() -> customerManagementApplicationService.archive(customerId.toString()))
                .isInstanceOf(CustomerAlreadyArchivedException.class);
    }

    @Test
    void shouldSuccessfullyChangeEmail() {
        CustomerInput customerInput = CustomerInputTestDataBuilder.aCustomer().build();

        CustomerId customerId = customerManagementApplicationService.create(customerInput);

        customerManagementApplicationService.changeEmail(customerId.toString(), "new@email.com");

        CustomerOutput customerOutput =
                customerQueryService.findById(customerId.toString());

        assertThat(customerOutput.getEmail()).isEqualTo("new@email.com");
    }

    @Test
    void shouldThrowExceptionWhenChangingEmailOfNonExistingCustomer() {
        assertThatThrownBy(() -> customerManagementApplicationService.changeEmail(DEFAULT_CUSTOMER_ID.toString(), "new@email.com"))
                .isInstanceOf(CustomerNotFoundException.class);
    }

    @Test
    void shouldThrowExceptionWhenChangingEmailOfArchivedCustomer() {
        CustomerInput customerInput = CustomerInputTestDataBuilder.aCustomer().build();

        CustomerId customerId = customerManagementApplicationService.create(customerInput);

        customerManagementApplicationService.archive(customerId.toString());

        assertThatThrownBy(() -> customerManagementApplicationService.changeEmail(customerId.toString(), "new@email.com"))
                .isInstanceOf(CustomerAlreadyArchivedException.class);
    }

    @Test
    void shouldThrowExceptionWhenChangingEmailWithInvalidFormat() {
        CustomerInput customerInput = CustomerInputTestDataBuilder.aCustomer().build();

        CustomerId customerId = customerManagementApplicationService.create(customerInput);

        assertThatThrownBy(() -> customerManagementApplicationService
                .changeEmail(customerId.toString(), "email.com"))
                .isInstanceOf(DomainException.class);
    }

    @Test
    void shouldThrowExceptionWhenEmailIsInUse() {
        CustomerInput customerInput = CustomerInputTestDataBuilder.aCustomer().build();

        CustomerId customerId = customerManagementApplicationService.create(customerInput);

        assertThatThrownBy(() -> customerManagementApplicationService
                .changeEmail(customerId.toString(), "johndoe@email.com"))
                .isInstanceOf(CustomerEmailIsInUseException.class);
    }
}