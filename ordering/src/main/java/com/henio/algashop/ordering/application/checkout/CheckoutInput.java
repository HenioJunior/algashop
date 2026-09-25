package com.henio.algashop.ordering.application.checkout;

import com.henio.algashop.ordering.application.order.query.BillingData;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CheckoutInput {
    private String shoppingCartId;
    private String paymentMethod;
    private ShippingInput shipping;
    private BillingData billing;
}
