package com.henio.algashop.ordering.domain.model.entity;

import com.henio.algashop.ordering.domain.model.valueobject.*;
import com.henio.algashop.ordering.domain.model.valueobject.id.CustomerId;

import java.time.LocalDate;

import static com.henio.algashop.ordering.domain.model.entity.CustomerTestDataBuilder.DEFAULT_CUSTOMER_ID;

public class OrderTestDataBuilder {

    private CustomerId customerId = DEFAULT_CUSTOMER_ID;

    private PaymentMethod paymentMethod = PaymentMethod.GATEWAY_BALANCE;

    private Shipping shipping = aShipping();

    private final Shipping pastDateShipping = aPastDateShipping();

    private Billing billing = aBilling();

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

    public static Billing aBilling() {
        return Billing.builder()
                .address(anAddress())
                .document(new Document("225-09-1992"))
                .phone(new Phone("123-111-9911"))
                .email(new Email("john.doe@email.com"))
                .fullName(new FullName("John", "Doe")).build();
    }

    public static Shipping aShipping() {
        return Shipping.builder()
                .address(anAddress())
                .recipient(
                        Recipient.builder()
                                .fullName(new FullName("John", "Doe"))
                                .document(new Document("112-33-2321"))
                                .phone(new Phone("111-441-1244"))
                                .build())
                .cost(new Money("10.00"))
                .expectedDate(LocalDate.now().plusDays(1))
                .build();
    }

    public static Shipping aPastDateShipping() {
        return Shipping.builder()
                .address(anAddress())
                .recipient(
                        Recipient.builder()
                                .fullName(new FullName("John", "Doe"))
                                .document(new Document("112-33-2321"))
                                .phone(new Phone("111-441-1244"))
                                .build())
                .cost(new Money("10.00"))
                .expectedDate(LocalDate.now().minusDays(7))
                .build();
    }

    public static Address anAddress() {
        return Address.builder()
                .street("Bourbon Street")
                .number("1234")
                .neighborhood("North Ville")
                .complement("apt. 11")
                .city("Montfort")
                .state("South Carolina")
                .zipCode(new ZipCode("79911")).build();
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

    public OrderTestDataBuilder billingInfo(Billing billing) {
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
