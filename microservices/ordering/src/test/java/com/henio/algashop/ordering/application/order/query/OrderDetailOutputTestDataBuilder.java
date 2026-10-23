package com.henio.algashop.ordering.application.order.query;

import com.henio.algashop.ordering.application.commons.AddressData;
import com.henio.algashop.ordering.domain.model.customer.CustomerId;
import com.henio.algashop.ordering.domain.model.order.OrderId;
import com.henio.algashop.ordering.domain.model.order.OrderItemId;
import com.henio.algashop.ordering.domain.model.product.ProductId;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.List;

public class OrderDetailOutputTestDataBuilder {

    public static OrderDetailOutput.OrderDetailOutputBuilder placedOrder() {
        return placedOrder(new OrderId().toString());
    }
    public static OrderDetailOutput.OrderDetailOutputBuilder placedOrder(String orderId) {
        return OrderDetailOutput.builder()
                .id(orderId)
                .customer(CustomerMinimalOutput.builder()
                        .id(new CustomerId().value().toLong())
                        .firstName("John")
                        .lastName("Doe")
                        .document("12345")
                        .email("johndoe@email.com")
                        .phone("1191234564")
                        .build())
                .totalItems(2)
                .totalAmount(new BigDecimal("60.48"))
                .placedAt(OffsetDateTime.now())
                .paidAt(null)
                .canceledAt(null)
                .readyAt(null)
                .status("PLACED")
                .paymentMethod("GATEWAY_BALANCE")
                .shipping(ShippingData.builder()
                        .cost(new BigDecimal("20.5"))
                        .expectedDate(LocalDate.now().plusDays(2))
                        .recipient(RecipientData.builder()
                                .firstName("John")
                                .lastName("Doe")
                                .document("12345")
                                .phone("5511912341234")
                                .build())
                        .address(AddressData.builder()
                                .street("Bourbon Street")
                                .number("2000")
                                .complement("apt 122")
                                .neighborhood("North Ville")
                                .city("Yostfort")
                                .state("South Carolina")
                                .zipCode("12321")
                                .build())
                        .build())
                .billing(BillingData.builder()
                        .firstName("John")
                        .lastName("Doe")
                        .document("12345")
                        .phone("5511912341234")
                        .address(AddressData.builder()
                                .street("Bourbon Street")
                                .number("2000")
                                .complement("apt 122")
                                .neighborhood("North Ville")
                                .city("Yostfort")
                                .state("South Carolina")
                                .zipCode("12321")
                                .build())
                        .build())
                .items(itemsOutput(orderId));
    }

    private static List<OrderItemDetailOutput> itemsOutput(String orderId) {
        return List.of(
                OrderItemDetailOutput.builder()
                        .id(new OrderItemId().toString())
                        .orderId(orderId)
                        .productId(new ProductId().value().toString())
                        .productName("Notebook Dive Gamer X11")
                        .price(new BigDecimal("19.99"))
                        .quantity(2)
                        .totalAmount(new BigDecimal("39.98"))
                        .build()
        );
    }
}

