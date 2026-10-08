package com.henio.algashop.billing.domain.model.invoice.payment;

import com.henio.algashop.billing.domain.model.invoice.Payer;
import com.henio.algashop.billing.domain.model.invoice.PaymentMethod;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.UUID;

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

    public UUID getInvoiceId() {
        return invoiceId;
    }

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public static Builder builder() {
        return new Builder();
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        PaymentRequest that = (PaymentRequest) o;
        return paymentMethod == that.paymentMethod && Objects.equals(amount, that.amount) && Objects.equals(invoiceId, that.invoiceId) && Objects.equals(creditCardId, that.creditCardId) && Objects.equals(payer, that.payer);
    }

    @Override
    public int hashCode() {
        return Objects.hash(paymentMethod, amount, invoiceId, creditCardId, payer);
    }

    public static class Builder {

        private PaymentMethod paymentMethod;
        private BigDecimal amount;
        private UUID invoiceId;
        private UUID creditCardId;
        private Payer payer;

        public Builder method(PaymentMethod paymentMethod) {
            this.paymentMethod = paymentMethod;
            return this;
        }

        public Builder amount(BigDecimal amount) {
            this.amount = amount;
            return this;
        }

        public Builder invoiceId(UUID invoiceId) {
            this.invoiceId = invoiceId;
            return this;
        }

        public Builder creditCardId(UUID creditCardId) {
            this.creditCardId = creditCardId;
            return this;
        }

        public Builder payer(Payer payer) {
            this.payer = payer;
            return this;
        }

        public PaymentRequest build() {
            return new PaymentRequest(paymentMethod, amount, invoiceId, creditCardId, payer);
        }
    }
}
