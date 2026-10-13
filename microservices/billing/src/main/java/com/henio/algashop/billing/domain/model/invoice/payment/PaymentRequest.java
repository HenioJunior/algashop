package com.henio.algashop.billing.domain.model.invoice.payment;

import com.henio.algashop.billing.domain.model.invoice.Payer;
import com.henio.algashop.billing.domain.model.invoice.PaymentMethod;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.UUID;

@Builder
@Getter
@EqualsAndHashCode
public class PaymentRequest {

    private PaymentMethod paymentMethod;
    private BigDecimal amount;
    private UUID invoiceId;
    private UUID creditCardId;
    private Payer payer;

    private PaymentRequest(PaymentMethod paymentMethod,
                          BigDecimal amount, UUID invoiceId,
                          UUID creditCardId, Payer payer) {
        Objects.requireNonNull(paymentMethod);
        Objects.requireNonNull(amount);
        Objects.requireNonNull(invoiceId);
        Objects.requireNonNull(payer);

        if (paymentMethod.equals(PaymentMethod.CREDIT_CARD)) {
            Objects.requireNonNull(creditCardId);
        }

        this.paymentMethod = paymentMethod;
        this.amount = amount;
        this.invoiceId = invoiceId;
        this.creditCardId = creditCardId;
        this.payer = payer;
    }
}
