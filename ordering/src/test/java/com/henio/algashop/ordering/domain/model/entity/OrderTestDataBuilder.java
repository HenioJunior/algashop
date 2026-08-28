package com.henio.algashop.ordering.domain.model.entity;

import com.henio.algashop.ordering.domain.model.valueobject.*;
import com.henio.algashop.ordering.domain.model.valueobject.id.CustomerId;

import static com.henio.algashop.ordering.domain.model.entity.CustomerTestDataBuilder.DEFAULT_CUSTOMER_ID;

public class OrderTestDataBuilder {

    private CustomerId customerId = DEFAULT_CUSTOMER_ID;

    private PaymentMethod paymentMethod = PaymentMethod.GATEWAY_BALANCE;

    private Shipping shipping = ShippingTestDataBuilder.aShipping().build();

    private Billing billing = BillingTestDataBuilder.aBilling().build();

    private boolean includeItems = true;

    private OrderStatus status = OrderStatus.DRAFT;

    private OrderTestDataBuilder() {
    }

    public static OrderTestDataBuilder anOrder() {
        return new OrderTestDataBuilder();
    }

    public Order build() {
        Order order = Order.draft(customerId);
        order.changeShipping(shipping);
        order.changeBilling(billing);
        order.changePaymentMethod(paymentMethod);

        if (includeItems) {
            order.addItem(ProductTestDataBuilder.aProduct().build(),
                    new Quantity(2)
            );

            order.addItem(ProductTestDataBuilder.aProductAltRamMemory().build(),
                    new Quantity(1)
            );
        }

        switch (this.status) {
            case DRAFT -> {
            }
            case PLACED -> {
                order.place();
            }
            case PAID -> {
                order.place();
                order.markAsPaid();
            }
            case READY -> {
                order.place();
                order.markAsPaid();
                order.markAsReady();
            }
            case CANCELED -> {
                order.cancel();
            }
        }

        return order;
    }

    public OrderTestDataBuilder customerId(CustomerId customerId) {
        this.customerId = customerId;
        return this;
    }

    public OrderTestDataBuilder paymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
        return this;
    }

    public OrderTestDataBuilder shipping(Shipping shipping) {
        this.shipping = shipping;
        return this;
    }

    public OrderTestDataBuilder billing(Billing billing) {
        this.billing = billing;
        return this;
    }

    public OrderTestDataBuilder withItems(boolean withItems) {
        this.includeItems = withItems;
        return this;
    }

    public OrderTestDataBuilder status(OrderStatus status) {
        this.status = status;
        return this;
    }
}
