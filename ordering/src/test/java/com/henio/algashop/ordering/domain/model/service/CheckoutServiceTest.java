package com.henio.algashop.ordering.domain.model.service;

import com.henio.algashop.ordering.domain.model.entity.*;
import com.henio.algashop.ordering.domain.model.exception.ShoppingCartCantProceedToCheckoutException;
import com.henio.algashop.ordering.domain.model.valueobject.*;
import com.henio.algashop.ordering.domain.model.valueobject.id.ProductId;
import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class CheckoutServiceTest {

    CheckoutService checkoutService = new CheckoutService();

    @Test
    void shouldCheckoutShoppingCart() {
        ShoppingCart shoppingCart = ShoppingCartTestDataBuilder.aShoppingCart().build();
        Billing billing = BillingTestDataBuilder.aBilling().build();
        Shipping shipping = ShippingTestDataBuilder.aShipping().build();
        PaymentMethod paymentMethod = PaymentMethod.CREDIT_CARD;

        Order order = checkoutService.checkout(
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
    void shouldNotCheckoutWithUnavailableItems() {
        ShoppingCart shoppingCart =
                ShoppingCartTestDataBuilder.aShoppingCart()
                        .withItems(false)
                        .build();

        ProductId productId = new ProductId();

        Product availableProduct = ProductTestDataBuilder.aProduct()
                .id(productId)
                .inStock(true)
                .build();

        shoppingCart.addItem(
                availableProduct,
                new Quantity(2)
        );

        Product unavailableProduct = ProductTestDataBuilder.aProduct()
                .id(productId)
                .inStock(false)
                .build();

        shoppingCart.refreshItem(unavailableProduct);

        Billing billing = BillingTestDataBuilder.aBilling().build();
        Shipping shipping = ShippingTestDataBuilder.aShipping().build();
        PaymentMethod paymentMethod = PaymentMethod.CREDIT_CARD;

        ThrowableAssert.ThrowingCallable checkoutTask = () -> checkoutService.checkout(
                shoppingCart,
                billing,
                shipping,
                paymentMethod
        );

        assertThat(shoppingCart.containsUnavailableItems()).isTrue();
        assertThatExceptionOfType(ShoppingCartCantProceedToCheckoutException.class).isThrownBy(checkoutTask);
        assertThat(shoppingCart.isEmpty()).isFalse();
    }

    @Test
    void shouldNotCheckoutWithZeroItems() {
        ShoppingCart shoppingCart =
                ShoppingCartTestDataBuilder.aShoppingCart()
                        .withItems(false).build();

        Billing billing = BillingTestDataBuilder.aBilling().build();
        Shipping shipping = ShippingTestDataBuilder.aShipping().build();
        PaymentMethod paymentMethod = PaymentMethod.CREDIT_CARD;

        ThrowableAssert.ThrowingCallable checkoutTask = () -> checkoutService.checkout(
                shoppingCart,
                billing,
                shipping,
                paymentMethod
        );

        assertThatExceptionOfType(ShoppingCartCantProceedToCheckoutException.class).isThrownBy(checkoutTask);
        assertThat(shoppingCart.isEmpty()).isTrue();
    }
}