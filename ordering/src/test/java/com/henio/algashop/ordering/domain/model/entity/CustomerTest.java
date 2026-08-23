package com.henio.algashop.ordering.domain.model.entity;

import com.henio.algashop.ordering.domain.model.exception.DomainException;
import com.henio.algashop.ordering.domain.model.valueobject.Email;
import com.henio.algashop.ordering.domain.model.valueobject.LoyaltyPoints;
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

        customer.addLoyaltyPoints(10);
        customer.addLoyaltyPoints(20);

        Assertions.assertThat(customer.loyaltyPoints()).isEqualTo(new LoyaltyPoints(30));
    }

    @Test
    void given_brandNewCustomer_whenAddInvalidLoyaltyPoints_shouldGenerateException() {
        Customer customer = CustomerTestDataBuilder.brandNewCustomer();

        Assertions.assertThatExceptionOfType(DomainException.class)
                .isThrownBy(()-> customer.addLoyaltyPoints(0));

        Assertions.assertThatExceptionOfType(DomainException.class)
                .isThrownBy(()-> customer.addLoyaltyPoints(-10));
    }
}
