package com.henio.algashop.ordering.application.checkout;

import com.henio.algashop.ordering.application.order.notification.NotifyOrderPlacedInput;
import com.henio.algashop.ordering.application.order.notification.OrderNotificationService;
import com.henio.algashop.ordering.domain.model.commons.Money;
import com.henio.algashop.ordering.domain.model.commons.Quantity;
import com.henio.algashop.ordering.domain.model.customer.CustomerTestDataBuilder;
import com.henio.algashop.ordering.domain.model.customer.Customers;
import com.henio.algashop.ordering.domain.model.order.Order;
import com.henio.algashop.ordering.domain.model.order.OrderId;
import com.henio.algashop.ordering.domain.model.order.OrderStatus;
import com.henio.algashop.ordering.domain.model.order.Orders;
import com.henio.algashop.ordering.domain.model.order.event.OrderPlacedEvent;
import com.henio.algashop.ordering.domain.model.order.shipping.ShippingCostService;
import com.henio.algashop.ordering.domain.model.product.Product;
import com.henio.algashop.ordering.domain.model.product.ProductTestDataBuilder;
import com.henio.algashop.ordering.domain.model.shoppingcart.ShoppingCart;
import com.henio.algashop.ordering.domain.model.shoppingcart.ShoppingCartTestDataBuilder;
import com.henio.algashop.ordering.domain.model.shoppingcart.ShoppingCarts;
import com.henio.algashop.ordering.domain.model.shoppingcart.exception.ShoppingCartCantProceedToCheckoutException;
import com.henio.algashop.ordering.domain.model.shoppingcart.exception.ShoppingCartNotFoundException;
import com.henio.algashop.ordering.infrastructure.listener.order.OrderEventListener;
import com.henio.algashop.ordering.infrastructure.persistence.customer.CustomerPersistenceEntityRepository;
import com.henio.algashop.ordering.infrastructure.persistence.order.OrderPersistenceEntityRepository;
import com.henio.algashop.ordering.infrastructure.persistence.shoppingcart.ShoppingCartPersistenceEntityRepository;
import io.hypersistence.tsid.TSID;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import static com.henio.algashop.ordering.domain.model.shoppingcart.ShoppingCartTestDataBuilder.DEFAULT_SHOPPING_CART_ID;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class CheckoutApplicationServiceIT {

    @MockitoSpyBean
    private OrderEventListener orderEventListener;

    @MockitoSpyBean
    private OrderNotificationService orderNotificationService;

    @Autowired
    private CheckoutApplicationService service;

    @Autowired
    private Orders orders;

    @Autowired
    private ShoppingCarts shoppingCarts;

    @Autowired
    private Customers customers;

    @Autowired
    private OrderPersistenceEntityRepository orderRepository;

    @Autowired
    private ShoppingCartPersistenceEntityRepository shoppingCartRepository;

    @Autowired
    private CustomerPersistenceEntityRepository customerRepository;

    @MockitoBean
    private ShippingCostService shippingCostService;

    @AfterEach
    void cleanup() {
        orderRepository.deleteAll();
        shoppingCartRepository.deleteAll();
        customerRepository.deleteAll();
    }

    @BeforeEach
    public void setup() {
        Mockito.when(shippingCostService.calculate(Mockito.any(ShippingCostService.CalculationRequest.class)))
                .thenReturn(new ShippingCostService.CalculationResult(
                        new Money("10.00"),
                        LocalDate.now().plusDays(3)
                ));

        if (!customers.exists(CustomerTestDataBuilder.DEFAULT_CUSTOMER_ID)) {
            customers.add(CustomerTestDataBuilder.existingCustomer().build());
        }
    }

    @Test
    void shouldCheckout() {
        Product product = ProductTestDataBuilder.aProduct().inStock(true).build();

        ShoppingCart shoppingCart = ShoppingCartTestDataBuilder.aShoppingCart().withItems(false).build();
        shoppingCart.addItem(product, new Quantity(1));
        shoppingCarts.add(shoppingCart);

        CheckoutInput input = CheckoutInputTestDataBuilder.aCheckoutInput()
                .shoppingCartId(shoppingCart.id().value().toString())
                .build();


        String orderId = service.checkout(input);
        OrderId createdOrderId = new OrderId(TSID.from(orderId));

        assertThat(orderId).isNotBlank();
        assertThat(orders.exists(createdOrderId)).isTrue();

        Optional<Order> createdOrder = orders.ofId(createdOrderId);

        assertThat(createdOrder)
                .isPresent()
                .get()
                .satisfies(order -> {
                    assertThat(order.status()).isEqualTo(OrderStatus.PLACED);
                    assertThat(order.totalAmount().value()).isGreaterThan(BigDecimal.ZERO);
                    assertThat(createdOrder.get().items()).hasSize(1);
                    assertThat(createdOrder.get().totalItems()).isEqualTo(new Quantity(1));
                });

        Optional<ShoppingCart> updatedCart = shoppingCarts.ofId(shoppingCart.id());
        assertThat(updatedCart)
                .isPresent()
                .get()
                .satisfies(cart -> assertThat(cart.isEmpty()).isTrue());

        Mockito.verify(orderNotificationService)
                .notifyOrder(
                        Mockito.any(NotifyOrderPlacedInput.class)
                );

        Mockito.verify(orderEventListener)
                .handleOrder(Mockito.any(OrderPlacedEvent.class));
    }

    @Test
    void shouldThrowShoppingCartNotFoundExceptionWhenCheckoutWithNonExistingShoppingCart() {
        CheckoutInput input = CheckoutInputTestDataBuilder.aCheckoutInput()
                .shoppingCartId(DEFAULT_SHOPPING_CART_ID.value().toString())
                .build();

        Assertions.assertThatExceptionOfType(ShoppingCartNotFoundException.class)
                .isThrownBy(() -> service.checkout(input));
    }

    @Test
    void shouldThrowShoppingCartCantProceedToCheckoutExceptionWhenCartIsEmpty() {
        ShoppingCart shoppingCart = ShoppingCartTestDataBuilder.aShoppingCart().withItems(false).build();
        shoppingCarts.add(shoppingCart);

        CheckoutInput input = CheckoutInputTestDataBuilder.aCheckoutInput()
                .shoppingCartId(shoppingCart.id().value().toString())
                .build();

        Assertions.assertThatExceptionOfType(ShoppingCartCantProceedToCheckoutException.class)
                .isThrownBy(() -> service.checkout(input));
    }

    @Test
    void shouldThrowShoppingCartCantProceedToCheckoutExceptionWhenCartContainsUnavailableItems() {
        Product product = ProductTestDataBuilder.aProduct().inStock(true).build();
        Product unavailableProduct = ProductTestDataBuilder.aProduct().id(product.id()).inStock(false).build();

        ShoppingCart shoppingCart = ShoppingCartTestDataBuilder.aShoppingCart().withItems(false).build();
        shoppingCart.addItem(product, new Quantity(1));
        shoppingCart.refreshItem(unavailableProduct);
        shoppingCarts.add(shoppingCart);

        CheckoutInput input = CheckoutInputTestDataBuilder.aCheckoutInput()
                .shoppingCartId(shoppingCart.id().value().toString())
                .build();

        Assertions.assertThatExceptionOfType(ShoppingCartCantProceedToCheckoutException.class)
                .isThrownBy(() -> service.checkout(input));
    }
}