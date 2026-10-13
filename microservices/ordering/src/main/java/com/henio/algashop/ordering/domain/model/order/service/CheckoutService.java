package com.henio.algashop.ordering.domain.model.order.service;

import com.henio.algashop.ordering.domain.model.commons.Money;
import com.henio.algashop.ordering.domain.model.customer.Customer;
import com.henio.algashop.ordering.domain.model.order.Billing;
import com.henio.algashop.ordering.domain.model.order.CustomerHaveFreeShippingSpecification;
import com.henio.algashop.ordering.domain.model.order.Order;
import com.henio.algashop.ordering.domain.model.order.PaymentMethod;
import com.henio.algashop.ordering.domain.model.order.shipping.Shipping;
import com.henio.algashop.ordering.domain.model.product.Product;
import com.henio.algashop.ordering.domain.model.shared.DomainService;
import com.henio.algashop.ordering.domain.model.shoppingcart.ShoppingCart;
import com.henio.algashop.ordering.domain.model.shoppingcart.ShoppingCartItem;
import com.henio.algashop.ordering.domain.model.shoppingcart.exception.ShoppingCartCantProceedToCheckoutException;
import lombok.RequiredArgsConstructor;

import java.util.Objects;

@DomainService
@RequiredArgsConstructor
public class CheckoutService {

    private final CustomerHaveFreeShippingSpecification customerHaveFreeShipping;
    
    public Order checkout(
            Customer customer,
            ShoppingCart shoppingCart,
            Billing billing,
            Shipping shipping,
            PaymentMethod paymentMethod
    ) {
        Objects.requireNonNull(customer, "Customer is required");
        Objects.requireNonNull(shoppingCart, "Shopping cart is required");
        Objects.requireNonNull(billing, "Billing is required");
        Objects.requireNonNull(shipping, "Shipping is required");
        Objects.requireNonNull(paymentMethod, "Payment method is required");


        if (shoppingCart.containsUnavailableItems() || shoppingCart.isEmpty()) {
            throw new ShoppingCartCantProceedToCheckoutException();        }

        Order order = Order.draft(shoppingCart.customerId());
        order.changeBilling(billing);

        if(isHaveFreeShipping(customer)) {
            Shipping freeShipping = shipping.toBuilder()
                    .cost(Money.ZERO)
                    .build();
            order.changeShipping(freeShipping);
        } else {
            order.changeShipping(shipping);
        }
        
        order.changePaymentMethod(paymentMethod);

        addShoppingCartItemsToOrder(shoppingCart, order);

        order.place();

        shoppingCart.empty();

        return order;
    }

    private static void addShoppingCartItemsToOrder(ShoppingCart shoppingCart, Order order) {
        for(ShoppingCartItem item : shoppingCart.items()) {
            Product product = Product.builder()
                    .id(item.productId())
                    .name(item.name())
                    .price(item.price())
                    .inStock(item.isAvailable())
                    .build();
            order.addItem(product, item.quantity());
        }
    }
    
    private boolean isHaveFreeShipping(Customer customer) {
        return customerHaveFreeShipping.isSatisfiedBy(customer);
    }
}
