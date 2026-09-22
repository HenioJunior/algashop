package com.henio.algashop.ordering.domain.model.order;

import com.henio.algashop.ordering.domain.model.commons.Money;
import com.henio.algashop.ordering.domain.model.commons.Quantity;
import com.henio.algashop.ordering.domain.model.customer.Customer;
import com.henio.algashop.ordering.domain.model.customer.CustomerTestDataBuilder;
import com.henio.algashop.ordering.domain.model.customer.LoyaltyPoints;
import com.henio.algashop.ordering.domain.model.order.service.CheckoutService;
import com.henio.algashop.ordering.domain.model.order.shipping.Shipping;
import com.henio.algashop.ordering.domain.model.order.shipping.ShippingTestDataBuilder;
import com.henio.algashop.ordering.domain.model.product.Product;
import com.henio.algashop.ordering.domain.model.product.ProductName;
import com.henio.algashop.ordering.domain.model.product.ProductTestDataBuilder;
import com.henio.algashop.ordering.domain.model.shoppingcart.ShoppingCart;
import com.henio.algashop.ordering.domain.model.shoppingcart.ShoppingCartTestDataBuilder;
import com.henio.algashop.ordering.domain.model.shoppingcart.exception.ShoppingCartCantProceedToCheckoutException;
import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class CheckoutServiceTest {

    private CheckoutService checkoutService;

    @Mock
    private Orders orders;

    @BeforeEach
    void setUp() {
        var customerHaveFreeShippingSpecification = new CustomerHaveFreeShippingSpecification(
                new LoyaltyPoints(100),
                orders,
                2,
                new LoyaltyPoints(2000)

        );
        checkoutService = new CheckoutService(customerHaveFreeShippingSpecification);
    }

    @Test
    void givenValidShoppingCart_whenCheckout_shouldReturnPlacedOrderAndEmptyShoppingCart() {
        Customer customer = CustomerTestDataBuilder.brandNewCustomer().build();

        ShoppingCart shoppingCart = ShoppingCart.startShopping(customer.id());
        shoppingCart.addItem(ProductTestDataBuilder.aProduct().build(), new Quantity(2));
        shoppingCart.addItem(ProductTestDataBuilder.aProductAltRamMemory().build(), new Quantity(1));

        Billing billing = BillingTestDataBuilder.aBilling().build();
        Shipping shipping = ShippingTestDataBuilder.aShipping().build();
        PaymentMethod paymentMethod = PaymentMethod.CREDIT_CARD;

        Money shoppingCartTotalAmount = shoppingCart.totalAmount();
        Quantity expectedOrderTotalItems = shoppingCart.totalItems();
        int expectedOrderItemsCount = shoppingCart.items().size();

        Order order = checkoutService.checkout(
                customer,
                shoppingCart,
                billing,
                shipping,
                paymentMethod
        );

        assertThat(order).isNotNull();
        assertThat(order.customerId()).isEqualTo(shoppingCart.customerId());
        assertThat(order.billing()).isEqualTo(billing);
        assertThat(order.shipping()).isEqualTo(shipping);
        assertThat(order.paymentMethod()).isEqualTo(paymentMethod);
        assertThat(order.status()).isEqualTo(OrderStatus.PLACED);
        assertThat(shoppingCart.isEmpty()).isTrue();

        Money expectedTotalAmountWithShipping = shoppingCartTotalAmount.add(shipping.cost());
        assertThat(order.totalAmount()).isEqualTo(expectedTotalAmountWithShipping);
        assertThat(order.totalItems()).isEqualTo(expectedOrderTotalItems);
        assertThat(order.items()).hasSize(expectedOrderItemsCount);

        assertThat(shoppingCart.isEmpty()).isTrue();
        assertThat(shoppingCart.totalAmount()).isEqualTo(Money.ZERO);
        assertThat(shoppingCart.totalItems()).isEqualTo(Quantity.ZERO);

        assertThat(order.items())
                .extracting(
                        OrderItem::productName,
                        OrderItem::price,
                        OrderItem::quantity
                )
                .containsExactlyInAnyOrder(
                        tuple(
                                new ProductName("Notebook X11"),
                                new Money("3000"),
                                new Quantity(2)
                        ),
                        tuple(
                                new ProductName("4GB RAM"),
                                new Money("200"),
                                new Quantity(1)
                        )
                );
    }

    @Test
    void givenShoppingCartWithUnavailableItems_whenCheckout_shouldThrowShoppingCartCantProceedToCheckoutException() {
        Customer customer = CustomerTestDataBuilder.brandNewCustomer().build();
        ShoppingCart shoppingCart =
                ShoppingCartTestDataBuilder.aShoppingCart()
                        .customerId(customer.id())
                        .withItems(false)
                        .build();
        Product product = ProductTestDataBuilder.aProduct().build();
        shoppingCart.addItem(product, new Quantity(1));

        Product unavailableProduct = ProductTestDataBuilder.aProduct()
                .id(product.id())
                .inStock(false)
                .build();

        shoppingCart.refreshItem(unavailableProduct);

        Billing billing = BillingTestDataBuilder.aBilling().build();
        Shipping shipping = ShippingTestDataBuilder.aShipping().build();
        PaymentMethod paymentMethod = PaymentMethod.CREDIT_CARD;

        ThrowableAssert.ThrowingCallable checkoutTask = () -> checkoutService.checkout(
                customer,
                shoppingCart,
                billing,
                shipping,
                paymentMethod
        );

        assertThat(shoppingCart.containsUnavailableItems()).isTrue();
        assertThatExceptionOfType(ShoppingCartCantProceedToCheckoutException.class)
                .isThrownBy(checkoutTask);
        assertThat(shoppingCart.items()).hasSize(1);
    }

    @Test
    void givenEmptyShoppingCart_whenCheckout_shouldThrowShoppingCartCantProceedToCheckoutException() {
        Customer customer = CustomerTestDataBuilder.brandNewCustomer().build();
        ShoppingCart shoppingCart =
                ShoppingCartTestDataBuilder.aShoppingCart()
                        .withItems(false).build();

        Billing billing = BillingTestDataBuilder.aBilling().build();
        Shipping shipping = ShippingTestDataBuilder.aShipping().build();
        PaymentMethod paymentMethod = PaymentMethod.CREDIT_CARD;

        ThrowableAssert.ThrowingCallable checkoutTask = () -> checkoutService.checkout(
                customer,
                shoppingCart,
                billing,
                shipping,
                paymentMethod
        );

        assertThatExceptionOfType(ShoppingCartCantProceedToCheckoutException.class)
                .isThrownBy(checkoutTask);
        assertThat(shoppingCart.isEmpty()).isTrue();
    }

    @Test
    void givenValidShoppingCartAndCustomerWithFreeShipping_whenCheckout_shouldReturnPlacedOrderWithFreeShipping() {
        Customer customer = CustomerTestDataBuilder
                .existingCustomer()
                .loyaltyPoints(new LoyaltyPoints(3000))
                .build();

        ShoppingCart shoppingCart = ShoppingCart.startShopping(customer.id());
        shoppingCart.addItem(ProductTestDataBuilder.aProduct().build(), new Quantity(2));
        shoppingCart.addItem(ProductTestDataBuilder.aProductAltRamMemory().build(), new Quantity(1));

        Billing billing = BillingTestDataBuilder.aBilling().build();
        Shipping shipping = ShippingTestDataBuilder.aShipping().build();
        PaymentMethod paymentMethod = PaymentMethod.CREDIT_CARD;

        Money shoppingCartTotalAmount = shoppingCart.totalAmount();
        Quantity expectedOrderTotalItems = shoppingCart.totalItems();
        int expectedOrderItemsCount = shoppingCart.items().size();

        Order order = checkoutService.checkout(
                customer,
                shoppingCart,
                billing,
                shipping,
                paymentMethod
        );

        assertThat(order).isNotNull();
        assertThat(order.customerId()).isEqualTo(shoppingCart.customerId());
        assertThat(order.billing()).isEqualTo(billing);
        assertThat(order.shipping()).isEqualTo(shipping.toBuilder().cost(Money.ZERO).build());
        assertThat(order.paymentMethod()).isEqualTo(paymentMethod);
        assertThat(order.status()).isEqualTo(OrderStatus.PLACED);
        assertThat(shoppingCart.isEmpty()).isTrue();

        assertThat(order.totalAmount()).isEqualTo(shoppingCartTotalAmount);
        assertThat(order.totalItems()).isEqualTo(expectedOrderTotalItems);
        assertThat(order.items()).hasSize(expectedOrderItemsCount);

        assertThat(shoppingCart.isEmpty()).isTrue();
        assertThat(shoppingCart.totalAmount()).isEqualTo(Money.ZERO);
        assertThat(shoppingCart.totalItems()).isEqualTo(Quantity.ZERO);
    }
}