package com.henio.algashop.ordering.domain.model.order.service;

import com.henio.algashop.ordering.domain.model.commons.Money;
import com.henio.algashop.ordering.domain.model.commons.Quantity;
import com.henio.algashop.ordering.domain.model.customer.Customer;
import com.henio.algashop.ordering.domain.model.order.Billing;
import com.henio.algashop.ordering.domain.model.order.CustomerHaveFreeShippingSpecification;
import com.henio.algashop.ordering.domain.model.order.Order;
import com.henio.algashop.ordering.domain.model.order.PaymentMethod;
import com.henio.algashop.ordering.domain.model.order.shipping.Shipping;
import com.henio.algashop.ordering.domain.model.product.Product;
import com.henio.algashop.ordering.domain.model.shared.DomainService;
import lombok.RequiredArgsConstructor;

@DomainService
@RequiredArgsConstructor
public class BuyNowService {

    private final CustomerHaveFreeShippingSpecification customerHaveFreeShipping;

    public Order buyNow(
            Product product,
            Customer customer,
            Billing billing,
            Shipping shipping,
            Quantity quantity,
            PaymentMethod paymentMethod
    ) {
        product.checkOutOfStock();

        Order order = Order.draft(customer.id());
        order.changeBilling(billing);
        order.changePaymentMethod(paymentMethod);
        order.addItem(product, quantity);

        if(isHaveFreeShipping(customer)) {
            Shipping freeShipping = shipping.toBuilder()
                    .cost(Money.ZERO)
                    .build();
            order.changeShipping(freeShipping);
        } else {
            order.changeShipping(shipping);
        }

        order.place();

        return order;
    }

    private boolean isHaveFreeShipping(Customer customer) {
        return customerHaveFreeShipping.isSatisfiedBy(customer);
    }
}
