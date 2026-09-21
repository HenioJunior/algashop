package com.henio.algashop.ordering.application.shoppingcart.management;

import com.henio.algashop.ordering.application.shoppingcart.notification.*;
import com.henio.algashop.ordering.domain.model.commons.Money;
import com.henio.algashop.ordering.domain.model.commons.Quantity;
import com.henio.algashop.ordering.domain.model.customer.*;
import com.henio.algashop.ordering.domain.model.customer.exception.CustomerAlreadyHaveShoppingCartException;
import com.henio.algashop.ordering.domain.model.customer.exception.CustomerNotFoundException;
import com.henio.algashop.ordering.domain.model.product.*;
import com.henio.algashop.ordering.domain.model.shared.IdGenerator;
import com.henio.algashop.ordering.domain.model.shoppingcart.ShoppingCart;
import com.henio.algashop.ordering.domain.model.shoppingcart.ShoppingCartId;
import com.henio.algashop.ordering.domain.model.shoppingcart.ShoppingCartItem;
import com.henio.algashop.ordering.domain.model.shoppingcart.event.ShoppingCartCreatedEvent;
import com.henio.algashop.ordering.domain.model.shoppingcart.event.ShoppingCartEmptiedEvent;
import com.henio.algashop.ordering.domain.model.shoppingcart.event.ShoppingCartItemAddedEvent;
import com.henio.algashop.ordering.domain.model.shoppingcart.event.ShoppingCartItemRemovedEvent;
import com.henio.algashop.ordering.domain.model.shoppingcart.exception.ShoppingCartNotFoundException;
import com.henio.algashop.ordering.domain.model.shoppingcart.ShoppingCarts;
import com.henio.algashop.ordering.infrastructure.listener.shoppingcart.ShoppingCartEventListener;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static com.henio.algashop.ordering.domain.model.customer.CustomerTestDataBuilder.DEFAULT_CUSTOMER_ID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

@SpringBootTest
@Transactional
class ShoppingCartManagementApplicationServiceIT {

    @MockitoSpyBean
    private ShoppingCartEventListener shoppingCartEventListener;

    @MockitoBean
    private ShoppingCartNotificationService shoppingCartNotificationService;

    @Autowired
    private ShoppingCartManagementApplicationService service;

    @Autowired
    private ShoppingCarts shoppingCarts;

    @Autowired
    private Customers customers;

    @MockitoBean
    private ProductCatalogService productCatalogService;

    @Test
    void shouldAddItemToShoppingCartSuccessfully() {
        Customer customer = CustomerTestDataBuilder.brandNewCustomer().build();
        customers.add(customer);

        ShoppingCart shoppingCart = ShoppingCart.startShopping(customer.id());

        shoppingCarts.add(shoppingCart);

        Product product = ProductTestDataBuilder.aProduct().inStock(true).build();
        Mockito.when(productCatalogService.ofId(product.id())).thenReturn(Optional.of(product));

        ShoppingCartItemInput input = ShoppingCartItemInput.builder()
                .shoppingCartId(shoppingCart.id().value().toString())
                .productId(product.id().value().toString())
                .quantity(2)
                .build();

        service.addItem(input);

        ShoppingCart updatedCart = shoppingCarts.ofId(shoppingCart.id()).orElseThrow();
        assertThat(updatedCart.items()).hasSize(1);
        assertThat(updatedCart.items().iterator().next().productId()).isEqualTo(product.id());
        assertThat(updatedCart.items().iterator().next().quantity().value()).isEqualTo(2);

        Mockito.verify(shoppingCartNotificationService)
                .notifyShoppingCartItemAdded(
                        Mockito.any(NotifyShoppingCartItemAddedInput.class)
                );

        Mockito.verify(shoppingCartEventListener)
                .handleShoppingCartEvent(Mockito.any(ShoppingCartItemAddedEvent.class));
    }

    @Test
    void shouldThrowShoppingCartNotFoundExceptionWhenAddingItemToNonExistingShoppingCart() {
        String nonExistingCartId = IdGenerator.generateTSID().toString();
        Product product = ProductTestDataBuilder.aProduct().inStock(true).build();
        Mockito.when(productCatalogService.ofId(product.id())).thenReturn(Optional.of(product));

        ShoppingCartItemInput input = ShoppingCartItemInput.builder()
                .shoppingCartId(nonExistingCartId)
                .productId(product.id().toString())
                .quantity(1)
                .build();

        assertThatExceptionOfType(ShoppingCartNotFoundException.class)
                .isThrownBy(() -> service.addItem(input));
    }

    @Test
    void shouldThrowProductNotFoundExceptionWhenAddingNonExistingProduct() {
        Customer customer = CustomerTestDataBuilder.brandNewCustomer().build();
        customers.add(customer);
        ShoppingCart shoppingCart = ShoppingCart.startShopping(customer.id());
        shoppingCarts.add(shoppingCart);

        String rawProductId = IdGenerator.generateTSID().toString();

        Mockito.when(productCatalogService.ofId(Mockito.any())).thenReturn(Optional.empty());

        ShoppingCartItemInput input = ShoppingCartItemInput.builder()
                .shoppingCartId(shoppingCart.id().value().toString())
                .productId(rawProductId)
                .quantity(1)
                .build();

        assertThatExceptionOfType(ProductNotFoundException.class)
                .isThrownBy(() -> service.addItem(input));
    }

    @Test
    void shouldThrowProductOutOfStockExceptionWhenAddingOutOfStockProduct() {
        Customer customer = CustomerTestDataBuilder.brandNewCustomer().build();
        customers.add(customer);
        ShoppingCart shoppingCart = ShoppingCart.startShopping(customer.id());
        shoppingCarts.add(shoppingCart);

        Product outOfStockProduct = ProductTestDataBuilder.aProduct().inStock(false).build();
        Mockito.when(productCatalogService.ofId(outOfStockProduct.id())).thenReturn(Optional.of(outOfStockProduct));

        ShoppingCartItemInput input = ShoppingCartItemInput.builder()
                .shoppingCartId(shoppingCart.id().value().toString())
                .productId(outOfStockProduct.id().value().toString())
                .quantity(1)
                .build();

        assertThatExceptionOfType(ProductOutOfStockException.class)
                .isThrownBy(() -> service.addItem(input));
    }

    @Test
    void shouldEmptyShoppingCartSuccessfully() {
        Customer customer = CustomerTestDataBuilder.brandNewCustomer().build();
        customers.add(customer);

        ShoppingCart shoppingCart = ShoppingCart.startShopping(customer.id());
        shoppingCarts.add(shoppingCart);

        Product product = ProductTestDataBuilder.aProduct().inStock(true).build();
        Mockito.when(productCatalogService.ofId(product.id())).thenReturn(Optional.of(product));

        ShoppingCartItemInput input = ShoppingCartItemInput.builder()
                .shoppingCartId(shoppingCart.id().value().toString())
                .productId(product.id().value().toString())
                .quantity(2)
                .build();

        service.addItem(input);

        service.empty(shoppingCart.id().value().toString());

        ShoppingCart updatedCart = shoppingCarts.ofId(shoppingCart.id())
                .orElseThrow();

        assertThat(updatedCart.items()).isEmpty();
        assertThat(updatedCart.totalItems()).isEqualTo(Quantity.ZERO);
        assertThat(updatedCart.totalAmount()).isEqualTo(Money.ZERO);

        Mockito.verify(shoppingCartNotificationService)
                .notifyShoppingCartEmptied(
                        Mockito.any(NotifyShoppingCartEmptiedInput.class)
                );

        Mockito.verify(shoppingCartEventListener)
                .handleShoppingCartEvent(
                        Mockito.any(ShoppingCartEmptiedEvent.class)
                );
    }

    @Test
    void shouldRemoveShoppingCartSuccessfully() {
        Customer customer = CustomerTestDataBuilder.brandNewCustomer().build();
        customers.add(customer);

        ShoppingCart shoppingCart = ShoppingCart.startShopping(customer.id());
        shoppingCarts.add(shoppingCart);

        Product product = ProductTestDataBuilder.aProduct().inStock(true).build();
        Mockito.when(productCatalogService.ofId(product.id())).thenReturn(Optional.of(product));

        ShoppingCartItemInput input = ShoppingCartItemInput.builder()
                .shoppingCartId(shoppingCart.id().value().toString())
                .productId(product.id().value().toString())
                .quantity(2)
                .build();

        service.addItem(input);

        ShoppingCart cartWithItem = shoppingCarts.ofId(shoppingCart.id())
                .orElseThrow();

        ShoppingCartItem item = cartWithItem.items()
                .iterator()
                .next();

        service.removeItem(
                shoppingCart.id().value().toString(),
                item.id().value().toString()
        );

        ShoppingCart updatedCart = shoppingCarts.ofId(shoppingCart.id())
                .orElseThrow();

        assertThat(updatedCart.items()).isEmpty();
        assertThat(updatedCart.totalItems()).isEqualTo(Quantity.ZERO);
        assertThat(updatedCart.totalAmount()).isEqualTo(Money.ZERO);

        Mockito.verify(shoppingCartNotificationService)
                .notifyShoppingCartItemRemoved(
                        Mockito.any(NotifyShoppingCartItemRemovedInput.class)
                );

        Mockito.verify(shoppingCartEventListener)
                .handleShoppingCartEvent(
                        Mockito.any(ShoppingCartItemRemovedEvent.class)
                );
    }

    @Test
    void shouldCreateNewShoppingCartForExistingCustomer() {
        Customer customer = CustomerTestDataBuilder.brandNewCustomer().build();
        customers.add(customer);

        ShoppingCartId shoppingCartId = service.createNew(customer.id().value().toString());

        Optional<ShoppingCart> createdCart = shoppingCarts.ofId(shoppingCartId);
        assertThat(createdCart).isPresent();
        assertThat(createdCart.get().customerId().value()).isEqualTo(customer.id().value());
        assertThat(createdCart.get().isEmpty()).isTrue();

        Mockito.verify(shoppingCartNotificationService)
                .notifyShoppingCartCreated(
                        Mockito.any(NotifyShoppingCartCreatedInput.class)
                );

        Mockito.verify(shoppingCartEventListener)
                .handleShoppingCartEvent(Mockito.any(ShoppingCartCreatedEvent.class));
    }

    @Test
    void shouldThrowCustomerNotFoundExceptionWhenCreatingNewShoppingCartForNonExistingCustomer() {
        assertThatExceptionOfType(CustomerNotFoundException.class)
                .isThrownBy(() -> service.createNew(DEFAULT_CUSTOMER_ID.toString()));
    }

    @Test
    void shouldThrowCustomerAlreadyHaveShoppingCartExceptionWhenCreatingNewShoppingCartForCustomerWithExistingCart() {
        Customer customer = CustomerTestDataBuilder.brandNewCustomer().build();
        customers.add(customer);
        ShoppingCart existingCart = ShoppingCart.startShopping(customer.id());
        shoppingCarts.add(existingCart);

        assertThatExceptionOfType(CustomerAlreadyHaveShoppingCartException.class)
                .isThrownBy(() -> service.createNew(customer.id().toString()));
    }

}