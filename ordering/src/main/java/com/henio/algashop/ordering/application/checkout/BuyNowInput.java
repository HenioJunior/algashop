package com.henio.algashop.ordering.application.checkout;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BuyNowInput {
    private ShippingInput shipping;
    private BillingData billing;
    private String productId;
    private String customerId;
    private Integer quantity;
    private String paymentMethod;
}
