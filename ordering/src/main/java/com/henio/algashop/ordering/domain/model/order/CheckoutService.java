package com.henio.algashop.ordering.domain.model.order;

import com.henio.algashop.ordering.domain.model.order.shipping.Shipping;
import com.henio.algashop.ordering.domain.model.shoppingcart.ShoppingCart;
import com.henio.algashop.ordering.domain.model.shoppingcart.ShoppingCartItem;
import com.henio.algashop.ordering.domain.model.shoppingcart.ShoppingCartCantProceedToCheckoutException;
import com.henio.algashop.ordering.domain.model.shared.DomainService;
import com.henio.algashop.ordering.domain.model.product.Product;

import java.util.Objects;

@DomainService
public class CheckoutService {

    public Order checkout(ShoppingCart shoppingCart,
                          Billing billing,
                          Shipping shipping,
                          PaymentMethod paymentMethod) {
        Objects.requireNonNull(shoppingCart, "Shopping cart is required");
        Objects.requireNonNull(billing, "Billing is required");
        Objects.requireNonNull(shipping, "Shipping is required");
        Objects.requireNonNull(paymentMethod, "Payment method is required");

        if (shoppingCart.containsUnavailableItems() || shoppingCart.isEmpty()) {
            throw new ShoppingCartCantProceedToCheckoutException();        }

        Order order = Order.draft(shoppingCart.customerId());
        order.changeBilling(billing);
        order.changeShipping(shipping);
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
}
