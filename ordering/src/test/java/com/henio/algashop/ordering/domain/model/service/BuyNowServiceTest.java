package com.henio.algashop.ordering.domain.model.service;


import com.henio.algashop.ordering.domain.model.entity.*;
import com.henio.algashop.ordering.domain.model.exception.ProductOutOfStockException;
import com.henio.algashop.ordering.domain.model.valueobject.*;
import com.henio.algashop.ordering.domain.model.valueobject.id.CustomerId;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class BuyNowServiceTest {

    BuyNowService buyNowService = new BuyNowService();

    @Test
    void shouldBuyNow() {
        Customer customer = CustomerTestDataBuilder.existingCustomer().build();
        CustomerId customerId = customer.id();
        Product product = ProductTestDataBuilder.aProduct().build();
        Billing billing = BillingTestDataBuilder.aBilling().build();
        Shipping shipping = ShippingTestDataBuilder.aShipping().build();
        PaymentMethod paymentMethod = PaymentMethod.CREDIT_CARD;

        Order order = buyNowService.buyNow(
                product,
                customerId,
                billing,
                shipping,
                new Quantity(2),
                paymentMethod
        );

        assertThat(order.customerId()).isEqualTo(customerId);
        assertThat(order.billing()).isEqualTo(billing);
        assertThat(order.shipping()).isEqualTo(shipping);
        assertThat(order.isPlaced()).isTrue();
        assertThat(order.items()).hasSize(1);
        assertThat(order.totalItems()).isEqualTo(new Quantity(2));
        assertThat(order.totalAmount()).isEqualTo(product.price()
                .multiply(new Quantity(2))
                .add(new Money(shipping.cost().value())));
        assertThat(order.paymentMethod()).isEqualTo(paymentMethod);
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
                        )
                );
    }

    @Test
    void shouldNotBuyNowWhenProductIsOutOfStock() {
        Product product = ProductTestDataBuilder
                .aProductUnavailable()
                .build();

        CustomerId customerId = CustomerId.generate();
        Billing billing = BillingTestDataBuilder.aBilling().build();
        Shipping shipping = ShippingTestDataBuilder.aShipping().build();
        Quantity quantity = new Quantity(1);
        PaymentMethod paymentMethod = PaymentMethod.CREDIT_CARD;

        assertThatExceptionOfType(ProductOutOfStockException.class)
                .isThrownBy(() -> buyNowService.buyNow(
                        product,
                        customerId,
                        billing,
                        shipping,
                        quantity,
                        paymentMethod
                ));
    }

    @Test
    void shouldNotBuyNowWithZeroQuantity() {
        Product product = ProductTestDataBuilder.aProduct().build();
        CustomerId customerId = CustomerId.generate();
        Billing billing = BillingTestDataBuilder.aBilling().build();
        Shipping shipping = ShippingTestDataBuilder.aShipping().build();
        Quantity quantity = new Quantity(0);
        PaymentMethod paymentMethod = PaymentMethod.CREDIT_CARD;

        assertThatIllegalArgumentException()
                .isThrownBy(() -> buyNowService.buyNow(
                        product,
                        customerId,
                        billing,
                        shipping,
                        quantity,
                        paymentMethod
                ));
    }
}