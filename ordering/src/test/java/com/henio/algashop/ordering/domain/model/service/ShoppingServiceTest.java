package com.henio.algashop.ordering.domain.model.service;

import com.henio.algashop.ordering.domain.model.entity.Customer;
import com.henio.algashop.ordering.domain.model.entity.CustomerTestDataBuilder;
import com.henio.algashop.ordering.domain.model.entity.ShoppingCart;
import com.henio.algashop.ordering.domain.model.entity.ShoppingCartTestDataBuilder;
import com.henio.algashop.ordering.domain.model.exception.CustomerAlreadyHaveShoppingCartException;
import com.henio.algashop.ordering.domain.model.exception.CustomerNotFoundException;
import com.henio.algashop.ordering.domain.model.repository.Customers;
import com.henio.algashop.ordering.domain.model.repository.ShoppingCarts;
import com.henio.algashop.ordering.domain.model.valueobject.id.CustomerId;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ShoppingServiceTest {

    @Mock
    private ShoppingCarts shoppingCarts;

    @Mock
    private Customers customers;

    @InjectMocks
    private ShoppingService shoppingService;

    @Test
    void shouldStartShopping() {
        Customer customer = CustomerTestDataBuilder.existingCustomer().build();
        CustomerId customerId = customer.id();

        when(customers.exists(customerId)).thenReturn(true);
        when(shoppingCarts.ofCustomer(customerId)).thenReturn(Optional.empty());

        ShoppingCart shoppingCart = shoppingService.startShopping(customerId);

        assertThat(shoppingCart).isNotNull();
        assertThat(shoppingCart.customerId()).isEqualTo(customerId);
        assertThat(shoppingCart.isEmpty()).isTrue();

        verify(customers).exists(customerId);
        verify(shoppingCarts).ofCustomer(customerId);
    }

    @Test
    void shouldNotStartShoppingWhenCustomerDoesNotExist() {
        CustomerId customerId = CustomerId.generate();

        when(customers.exists(customerId)).thenReturn(false);

        assertThatExceptionOfType(CustomerNotFoundException.class)
                .isThrownBy(() -> shoppingService.startShopping(customerId));

        verify(customers).exists(customerId);
        verify(shoppingCarts, never()).ofCustomer(any());
    }

    @Test
    void shouldNotStartShoppingWhenCustomerAlreadyHasShoppingCart() {
        Customer customer = CustomerTestDataBuilder.existingCustomer().build();
        CustomerId customerId = customer.id();

        ShoppingCart existingShoppingCart =
                ShoppingCartTestDataBuilder.aShoppingCart()
                        .customerId(customerId)
                        .build();

        when(customers.exists(customerId)).thenReturn(true);
        when(shoppingCarts.ofCustomer(customerId))
                .thenReturn(Optional.of(existingShoppingCart));

        assertThatExceptionOfType(CustomerAlreadyHaveShoppingCartException.class)
                .isThrownBy(() -> shoppingService.startShopping(customerId));

        verify(customers).exists(customerId);
        verify(shoppingCarts).ofCustomer(customerId);
    }
}