package com.henio.algashop.ordering.domain.model.service;

import com.henio.algashop.ordering.domain.model.entity.ShoppingCart;
import com.henio.algashop.ordering.domain.model.exception.CustomerAlreadyHaveShoppingCartException;
import com.henio.algashop.ordering.domain.model.exception.CustomerNotFoundException;
import com.henio.algashop.ordering.domain.model.repository.Customers;
import com.henio.algashop.ordering.domain.model.repository.ShoppingCarts;
import com.henio.algashop.ordering.domain.model.utility.DomainService;
import com.henio.algashop.ordering.domain.model.valueobject.id.CustomerId;
import lombok.RequiredArgsConstructor;

import java.util.Objects;

@DomainService
@RequiredArgsConstructor
public class ShoppingService {

    private final Customers customers;
    private final ShoppingCarts shoppingCarts;

    public ShoppingCart startShopping(CustomerId customerId) {
        Objects.requireNonNull(customerId, "Customer ID is required");

        if(!customers.exists(customerId)) {
            throw new CustomerNotFoundException(customerId);
        }

        if(shoppingCarts.ofCustomer(customerId).isPresent()) {
            throw new CustomerAlreadyHaveShoppingCartException(customerId);
        }

        return ShoppingCart.startShopping(customerId);
    }
}
