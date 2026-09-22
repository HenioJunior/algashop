package com.henio.algashop.ordering.domain.model.order;

import com.henio.algashop.ordering.domain.model.commons.Money;
import com.henio.algashop.ordering.domain.model.commons.Quantity;
import com.henio.algashop.ordering.domain.model.customer.Customer;
import com.henio.algashop.ordering.domain.model.customer.CustomerId;
import com.henio.algashop.ordering.domain.model.customer.CustomerTestDataBuilder;
import com.henio.algashop.ordering.domain.model.customer.LoyaltyPoints;
import com.henio.algashop.ordering.domain.model.order.service.BuyNowService;
import com.henio.algashop.ordering.domain.model.order.shipping.Shipping;
import com.henio.algashop.ordering.domain.model.order.shipping.ShippingTestDataBuilder;
import com.henio.algashop.ordering.domain.model.product.Product;
import com.henio.algashop.ordering.domain.model.product.ProductOutOfStockException;
import com.henio.algashop.ordering.domain.model.product.ProductTestDataBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Year;

import static org.assertj.core.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class BuyNowServiceTest {

    private BuyNowService buyNowService;

    @Mock
    private Orders orders;

    @BeforeEach
    void setUp() {
        var customerHaveFreeShippingSpecification = new CustomerHaveFreeShippingSpecification(
                orders,
                100,
                2,
                2000
        );
        buyNowService = new BuyNowService(customerHaveFreeShippingSpecification);
    }

    @Test
    void givenValidProductAndDetails_whenBuyNow_shouldReturnPlacedOrder() {
        Product product = ProductTestDataBuilder.aProduct().build();
        Customer customer = CustomerTestDataBuilder.brandNewCustomer().build();
        Billing billing = BillingTestDataBuilder.aBilling().build();
        Shipping shipping = ShippingTestDataBuilder.aShipping().build();
        Quantity quantity = new Quantity(2);
        PaymentMethod paymentMethod = PaymentMethod.CREDIT_CARD;

        Order order = buyNowService.buyNow(
                product,
                customer,
                billing,
                shipping,
                quantity,
                paymentMethod
        );

        assertThat(order).isNotNull();
        assertThat(order.id()).isNotNull();
        assertThat(order.customerId()).isEqualTo(customer.id());
        assertThat(order.billing()).isEqualTo(billing);
        assertThat(order.shipping()).isEqualTo(shipping);
        assertThat(order.paymentMethod()).isEqualTo(paymentMethod);
        assertThat(order.isPlaced()).isTrue();

        assertThat(order.items()).hasSize(1);
        assertThat(order.items().iterator().next().productId()).isEqualTo(product.id());
        assertThat(order.items().iterator().next().quantity()).isEqualTo(quantity);
        assertThat(order.items().iterator().next().price()).isEqualTo(product.price());

        Money expectedTotalAmount = product.price().multiply(quantity).add(shipping.cost());
        assertThat(order.totalAmount()).isEqualTo(expectedTotalAmount);
        assertThat(order.totalItems()).isEqualTo(quantity);
    }

    @Test
    void shouldNotBuyNowWhenProductIsOutOfStock() {
        Product product = ProductTestDataBuilder
                .aProductUnavailable()
                .build();

        Customer customer = CustomerTestDataBuilder.brandNewCustomer().build();
        Billing billing = BillingTestDataBuilder.aBilling().build();
        Shipping shipping = ShippingTestDataBuilder.aShipping().build();
        Quantity quantity = new Quantity(1);
        PaymentMethod paymentMethod = PaymentMethod.CREDIT_CARD;

        assertThatExceptionOfType(ProductOutOfStockException.class)
                .isThrownBy(() -> buyNowService.buyNow(
                        product,
                        customer,
                        billing,
                        shipping,
                        quantity,
                        paymentMethod
                ));
    }

    @Test
    void shouldNotBuyNowWithZeroQuantity() {
        Product product = ProductTestDataBuilder.aProduct().build();
        Customer customer = CustomerTestDataBuilder.brandNewCustomer().build();
        Billing billing = BillingTestDataBuilder.aBilling().build();
        Shipping shipping = ShippingTestDataBuilder.aShipping().build();
        Quantity quantity = new Quantity(0);
        PaymentMethod paymentMethod = PaymentMethod.CREDIT_CARD;

        assertThatIllegalArgumentException()
                .isThrownBy(() -> buyNowService.buyNow(
                        product,
                        customer,
                        billing,
                        shipping,
                        quantity,
                        paymentMethod
                ));
    }

    @Test
    void givenCustomerWithFreeShipping_whenBuyNow_shouldReturnPlacedOrderWithFreeShipping() {
        Mockito.when(orders.salesQuantityByCustomerInYear(
                Mockito.any(CustomerId.class),
                Mockito.any(Year.class)
        )).thenReturn(2L);

        Product product = ProductTestDataBuilder.aProduct().build();
        Customer customer = CustomerTestDataBuilder.existingCustomer().loyaltyPoints(new LoyaltyPoints(100)).build();
        Billing billing = BillingTestDataBuilder.aBilling().build();
        Shipping shipping = ShippingTestDataBuilder.aShipping().build();
        Quantity quantity = new Quantity(3);
        PaymentMethod paymentMethod = PaymentMethod.CREDIT_CARD;

        Order order = buyNowService.buyNow(product, customer, billing, shipping, quantity, paymentMethod);

        assertThat(order).isNotNull();
        assertThat(order.id()).isNotNull();
        assertThat(order.customerId()).isEqualTo(customer.id());
        assertThat(order.billing()).isEqualTo(billing);
        assertThat(order.shipping()).isEqualTo(shipping.toBuilder().cost(Money.ZERO).build());
        assertThat(order.paymentMethod()).isEqualTo(paymentMethod);
        assertThat(order.isPlaced()).isTrue();

        assertThat(order.items()).hasSize(1);
        assertThat(order.items().iterator().next().productId()).isEqualTo(product.id());
        assertThat(order.items().iterator().next().quantity()).isEqualTo(quantity);
        assertThat(order.items().iterator().next().price()).isEqualTo(product.price());

        Money expectedTotalAmount = product.price().multiply(quantity);
        assertThat(order.totalAmount()).isEqualTo(expectedTotalAmount);
        assertThat(order.shipping().cost()).isEqualTo(Money.ZERO);
        assertThat(order.totalItems()).isEqualTo(quantity);
    }
}