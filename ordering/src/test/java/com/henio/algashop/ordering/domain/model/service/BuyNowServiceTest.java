package com.henio.algashop.ordering.domain.model.service;


import com.henio.algashop.ordering.domain.model.entity.*;
import com.henio.algashop.ordering.domain.model.valueobject.*;
import com.henio.algashop.ordering.domain.model.valueobject.id.CustomerId;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

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

        Assertions.assertThat(order.customerId()).isEqualTo(customerId);
        Assertions.assertThat(order.billing()).isEqualTo(billing);
        Assertions.assertThat(order.shipping()).isEqualTo(shipping);
        Assertions.assertThat(order.isPlaced()).isTrue();
        Assertions.assertThat(order.items()).hasSize(1);
        Assertions.assertThat(order.totalItems()).isEqualTo(new Quantity(2));
        Assertions.assertThat(order.totalAmount()).isEqualTo(product.price()
                .multiply(new Quantity(2))
                .add(new Money(shipping.cost().value())));
        Assertions.assertThat(order.paymentMethod()).isEqualTo(paymentMethod);
    }
}