package com.henio.algashop.ordering.domain.model.customer;

import com.henio.algashop.ordering.domain.model.customer.event.CustomerArchivedEvent;
import com.henio.algashop.ordering.domain.model.customer.event.CustomerRegisteredEvent;
import com.henio.algashop.ordering.domain.model.shared.DomainException;
import com.henio.algashop.ordering.domain.model.commons.Email;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class CustomerTest {

    @Test
    void given_invalidEmail_whenTryCreateCustomer_shouldGenerateException() {
        Customer customer = CustomerTestDataBuilder.brandNewCustomer().build();

        Assertions.assertThatExceptionOfType(DomainException.class).isThrownBy(
                () -> customer.changeEmail(new Email("invalid"))
        );
    }

    @Test
    void given_invalidEmail_whenTryUpdatedCustomerEmail_shouldGenerateException() {
        Customer customer = CustomerTestDataBuilder.brandNewCustomer().build();

        Assertions.assertThatExceptionOfType(DomainException.class)
                .isThrownBy(() ->
                        customer.changeEmail(new Email("invalid"))
                );
    }

    @Test
    void given_brandNewCustomer_whenAddLoyaltyPoints_shouldSumPoints() {
        Customer customer = CustomerTestDataBuilder.brandNewCustomer().build();

        customer.addLoyaltyPoints(new LoyaltyPoints(10));
        customer.addLoyaltyPoints(new LoyaltyPoints(20));

        assertThat(customer.loyaltyPoints()).isEqualTo(new LoyaltyPoints(30));
    }

    @Test
    void givenBrandNewCustomer_whenAddZeroLoyaltyPoints_shouldNotChangeLoyaltyPoints() {
        Customer customer = CustomerTestDataBuilder.brandNewCustomer().build();

        LoyaltyPoints currentPoints = customer.loyaltyPoints();

        customer.addLoyaltyPoints(LoyaltyPoints.ZERO);

        assertThat(customer.loyaltyPoints())
                .isEqualTo(currentPoints);
    }

    @Test
    void givenNegativeValue_whenCreateLoyaltyPoints_shouldGenerateException() {
        Assertions.assertThatExceptionOfType(DomainException.class)
                .isThrownBy(() -> new LoyaltyPoints(-10));
    }

    @Test
    void givenValidData_whenCreateBrandNewCostumer_shouldGenerateCustomerRegisteredEvent() {
        Customer customer = CustomerTestDataBuilder.brandNewCustomer().build();

        assertThat(customer.domainEvents())
                .containsExactly(new CustomerRegisteredEvent(
                        customer.id(),
                        customer.registeredAt(),
                        customer.fullName(),
                        customer.email())
                );
    }

    @Test
    void givenUnarchivedCustomer_whenArchive_shouldGenerateCustomerArchivedEvent() {
        Customer customer = CustomerTestDataBuilder
                .existingCustomer()
                .archived(false)
                .archivedAt(null)
                .build();

        customer.archive();

        CustomerArchivedEvent event = new CustomerArchivedEvent(customer.id(), customer.archivedAt());

        assertThat(customer.domainEvents()).contains(event);
    }
}
