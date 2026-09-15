package com.henio.algashop.ordering.application.shoppingcart.management;

import com.henio.algashop.ordering.domain.model.customer.Customer;
import com.henio.algashop.ordering.domain.model.customer.CustomerTestDataBuilder;
import com.henio.algashop.ordering.domain.model.customer.Customers;
import com.henio.algashop.ordering.domain.model.product.Product;
import com.henio.algashop.ordering.domain.model.product.ProductCatalogService;
import com.henio.algashop.ordering.domain.model.product.ProductTestDataBuilder;
import com.henio.algashop.ordering.domain.model.shoppingcart.ShoppingCart;
import com.henio.algashop.ordering.domain.model.shoppingcart.ShoppingCartTestDataBuilder;
import com.henio.algashop.ordering.domain.model.shoppingcart.ShoppingCarts;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@SpringBootTest
@Transactional
@RequiredArgsConstructor
class ShoppingCartManagementApplicationServiceIT {

    private final ShoppingCartManagementApplicationService service;
    private final ShoppingCarts shoppingCarts;
    private final Customers customers;

    @MockitoBean
    private final ProductCatalogService productCatalogService;

    @Test
    void shouldAddItemToShoppingCartSuccessfully() {
        Customer customer = CustomerTestDataBuilder.brandNewCustomer().build();
        customers.add(customer);

        ShoppingCart shoppingCart = ShoppingCart.startShopping(customer.id());
        shoppingCarts.add(shoppingCart);

        Product product = ProductTestDataBuilder.aProduct().inStock(true).build();
        Mockito.when(productCatalogService.ofId(product.id())).thenReturn(Optional.of(product));


    }

}