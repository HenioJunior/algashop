package com.henio.algashop.ordering.application.checkout;

import com.henio.algashop.ordering.domain.model.commons.ZipCode;
import com.henio.algashop.ordering.domain.model.customer.Customer;
import com.henio.algashop.ordering.domain.model.customer.Customers;
import com.henio.algashop.ordering.domain.model.customer.exception.CustomerNotFoundException;
import com.henio.algashop.ordering.domain.model.order.service.CheckoutService;
import com.henio.algashop.ordering.domain.model.order.Order;
import com.henio.algashop.ordering.domain.model.order.Orders;
import com.henio.algashop.ordering.domain.model.order.PaymentMethod;
import com.henio.algashop.ordering.domain.model.order.shipping.OriginAddressService;
import com.henio.algashop.ordering.domain.model.order.shipping.ShippingCostService;
import com.henio.algashop.ordering.domain.model.shoppingcart.ShoppingCart;
import com.henio.algashop.ordering.domain.model.shoppingcart.ShoppingCartId;
import com.henio.algashop.ordering.domain.model.shoppingcart.exception.ShoppingCartNotFoundException;
import com.henio.algashop.ordering.domain.model.shoppingcart.ShoppingCarts;
import io.hypersistence.tsid.TSID;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class CheckoutApplicationService {

    private final Orders orders;
    private final ShoppingCarts shoppingCarts;
    private final CheckoutService checkoutService;
    private final Customers customers;

    private final BillingInputDisassembler billingInputDisassembler;
    private final ShippingInputDisassembler shippingInputDisassembler;

    private final ShippingCostService shippingCostService;
    private final OriginAddressService originAddressService;

    @Transactional
    public String checkout(CheckoutInput input) {
        Objects.requireNonNull(input, "CheckoutInput must not be null");

        ShoppingCartId shoppingCartId = new ShoppingCartId(TSID.from(input.getShoppingCartId()));

        PaymentMethod paymentMethod = PaymentMethod.valueOf(input.getPaymentMethod());

        ShoppingCart shoppingCart = shoppingCarts.ofId(shoppingCartId).orElseThrow(
                () -> new ShoppingCartNotFoundException(shoppingCartId));

        Customer customer = customers.ofId(shoppingCart.customerId())
                .orElseThrow(() -> new CustomerNotFoundException(shoppingCart.customerId()));

        var shippingCalculationResult = calculateShippingCost(input.getShipping());

        Order order = checkoutService.checkout(
                customer,
                shoppingCart,
                billingInputDisassembler.toDomainModel(input.getBilling()),
                shippingInputDisassembler.toDomainModel(input.getShipping(), shippingCalculationResult),
                paymentMethod);

        orders.add(order);
        shoppingCarts.add(shoppingCart);

        return order.id().toString();
    }

    private ShippingCostService.CalculationResult calculateShippingCost(ShippingInput shipping) {
        ZipCode origin = originAddressService.originAddress().zipCode();
        ZipCode destination = new ZipCode(shipping.getAddress().getZipCode());
        return shippingCostService.calculate(new ShippingCostService.CalculationRequest(origin, destination));
    }
}
