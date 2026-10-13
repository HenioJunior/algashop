package com.henio.algashop.ordering.application.shoppingcart.query;

import com.henio.algashop.ordering.domain.model.commons.Quantity;
import com.henio.algashop.ordering.domain.model.customer.Customer;
import com.henio.algashop.ordering.domain.model.customer.CustomerId;
import com.henio.algashop.ordering.domain.model.customer.CustomerTestDataBuilder;
import com.henio.algashop.ordering.domain.model.customer.Customers;
import com.henio.algashop.ordering.domain.model.product.Product;
import com.henio.algashop.ordering.domain.model.product.ProductTestDataBuilder;
import com.henio.algashop.ordering.domain.model.shoppingcart.*;
import com.henio.algashop.ordering.domain.model.shoppingcart.exception.ShoppingCartNotFoundException;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Transactional
class ShoppingCartQueryServiceIT {

    @Autowired
    private ShoppingCartQueryService service;

    @Autowired
    private Customers customers;

    @Autowired
    private ShoppingCarts shoppingCarts;

    @Test
    void shouldReturnShoppingCartById() {
        Customer customer = CustomerTestDataBuilder.existingCustomer()
                .build();
        customers.add(customer);

        ShoppingCart shoppingCart = ShoppingCartTestDataBuilder
                .aShoppingCart()
                .customerId(customer.id())
                .withoutItems()
                .build();

        Product product = ProductTestDataBuilder
                .aProduct()
                .build();

        Quantity quantity = new Quantity(2);

        shoppingCart.addItem(product, quantity);

        shoppingCarts.add(shoppingCart);

        ShoppingCartOutput shoppingCartFromDb =
                service.findById(shoppingCart.id().value().toString());

        assertThat(shoppingCartFromDb).isNotNull();

        assertThat(shoppingCartFromDb.getId())
                .isEqualTo(shoppingCart.id().value().toString());

        assertThat(shoppingCartFromDb.getCustomerId())
                .isEqualTo(customer.id().value().toString());

        assertThat(shoppingCartFromDb.getTotalItems())
                .isEqualTo(quantity.value());

        assertThat(shoppingCartFromDb.getTotalAmount())
                .isEqualByComparingTo(shoppingCart.totalAmount().value());

        assertThat(shoppingCartFromDb.getItems())
                .hasSize(1);

        ShoppingCartItemOutput item =
                shoppingCartFromDb.getItems().getFirst();

        assertThat(item.getProductId())
                .isEqualTo(product.id().toString());

        assertThat(item.getQuantity())
                .isEqualTo(quantity.value());

        assertThat(item.getName())
                .isEqualTo(product.name().toString());

        assertThat(item.getPrice())
                .isEqualByComparingTo(product.price().value());

        assertThat(item.getTotalAmount())
                .isEqualByComparingTo(
                        product.price().multiply(quantity).value()
                );
    }

    @Test
    void shouldThrowExceptionWhenShoppingCartNotFoundById() {
        assertThatThrownBy(() -> service.findById(new ShoppingCartId().value().toString()))
                .isInstanceOf(ShoppingCartNotFoundException.class);
    }

    @Test
    void shouldReturnShoppingCartByCustomerId() {
        Customer customer = CustomerTestDataBuilder.existingCustomer()
                .build();
        customers.add(customer);

        ShoppingCart shoppingCart = ShoppingCartTestDataBuilder
                .aShoppingCart()
                .customerId(customer.id())
                .withoutItems()
                .build();

        Product product = ProductTestDataBuilder
                .aProduct()
                .build();

        Quantity quantity = new Quantity(2);

        shoppingCart.addItem(product, quantity);

        shoppingCarts.add(shoppingCart);

        ShoppingCartOutput shoppingCartFromDb =
                service.findByCustomerId(customer.id().value().toString());

        assertThat(shoppingCartFromDb).isNotNull();

        assertThat(shoppingCartFromDb.getId())
                .isEqualTo(shoppingCart.id().value().toString());

        assertThat(shoppingCartFromDb.getCustomerId())
                .isEqualTo(shoppingCart.customerId().value().toString());

        assertThat(shoppingCartFromDb.getTotalItems())
                .isEqualTo(quantity.value());

        assertThat(shoppingCartFromDb.getTotalAmount())
                .isEqualByComparingTo(shoppingCart.totalAmount().value());

        assertThat(shoppingCartFromDb.getItems())
                .hasSize(1);

        ShoppingCartItemOutput item =
                shoppingCartFromDb.getItems().getFirst();

        assertThat(item.getProductId())
                .isEqualTo(product.id().toString());

        assertThat(item.getQuantity())
                .isEqualTo(quantity.value());

        assertThat(item.getName())
                .isEqualTo(product.name().toString());

        assertThat(item.getPrice())
                .isEqualByComparingTo(product.price().value());

        assertThat(item.getTotalAmount())
                .isEqualByComparingTo(
                        product.price().multiply(quantity).value()
                );
    }

    @Test
    void shouldThrowExceptionWhenShoppingCartNotFoundByCustomerId() {
        assertThatThrownBy(() -> service.findByCustomerId(new CustomerId().value().toString()))
                .isInstanceOf(ShoppingCartNotFoundException.class);
    }
}