package com.henio.algashop.ordering.domain.model.customer;

import com.henio.algashop.ordering.domain.model.DomainException;
import com.henio.algashop.ordering.domain.model.commons.Email;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class CustomerTest {

    @Test
    void given_invalidEmail_whenTryCreateCustomer_shouldGenerateException() {

        Assertions.assertThatExceptionOfType(DomainException.class).isThrownBy(
                () -> CustomerTestDataBuilder.brandNewCustomer().changeEmail(new Email("invalid"))
        );
    }

    @Test
    void given_invalidEmail_whenTryUpdatedCustomerEmail_shouldGenerateException() {
        Customer customer = CustomerTestDataBuilder.brandNewCustomer();

        Assertions.assertThatExceptionOfType(DomainException.class)
                .isThrownBy(() ->
                        customer.changeEmail(new Email("invalid"))
                );
    }

    @Test
    void given_brandNewCustomer_whenAddLoyaltyPoints_shouldSumPoints() {
        Customer customer = CustomerTestDataBuilder.brandNewCustomer();

        customer.addLoyaltyPoints(new LoyaltyPoints(10));
        customer.addLoyaltyPoints(new LoyaltyPoints(20));

        Assertions.assertThat(customer.loyaltyPoints()).isEqualTo(new LoyaltyPoints(30));
    }

    @Test
    void givenBrandNewCustomer_whenAddZeroLoyaltyPoints_shouldNotChangeLoyaltyPoints() {
        Customer customer = CustomerTestDataBuilder.brandNewCustomer();

        LoyaltyPoints currentPoints = customer.loyaltyPoints();

        customer.addLoyaltyPoints(LoyaltyPoints.ZERO);

        Assertions.assertThat(customer.loyaltyPoints())
                .isEqualTo(currentPoints);
    }

    @Test
    void givenNegativeValue_whenCreateLoyaltyPoints_shouldGenerateException() {
        Assertions.assertThatExceptionOfType(DomainException.class)
                .isThrownBy(() -> new LoyaltyPoints(-10));
    }
}
