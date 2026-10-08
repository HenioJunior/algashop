package com.henio.algashop.billing.domain.model.invoice.payment;

import com.henio.algashop.billing.domain.model.FieldValidations;
import com.henio.algashop.billing.domain.model.invoice.PaymentMethod;
import jakarta.persistence.Embeddable;

import java.util.Objects;
import java.util.UUID;

@Embeddable
public class Payment {

    private String gatewayCode;
    private UUID invoiceId;
    private PaymentMethod method;
    private PaymentStatus status;

    protected Payment() {}

    private Payment(String gatewayCode, UUID invoiceId, PaymentMethod method, PaymentStatus status) {
        FieldValidations.requiresNonBlank(gatewayCode);
        Objects.requireNonNull(invoiceId);
        Objects.requireNonNull(method);
        Objects.requireNonNull(status);
        this.gatewayCode = gatewayCode;
        this.invoiceId = invoiceId;
        this.method = method;
        this.status = status;
    }

    public static Builder builder() {
        return new Builder();
    }

    public String getGatewayCode() {
        return gatewayCode;
    }

    public PaymentMethod getMethod() {
        return method;
    }

    public PaymentStatus getStatus() {
        return status;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Payment payment = (Payment) o;
        return Objects.equals(gatewayCode, payment.gatewayCode) && Objects.equals(invoiceId, payment.invoiceId) && method == payment.method && status == payment.status;
    }

    @Override
    public int hashCode() {
        return Objects.hash(gatewayCode, invoiceId, method, status);
    }

    public static class Builder {

        private String gatewayCode;
        private UUID invoiceId;
        private PaymentMethod method;
        private PaymentStatus status;

        public Builder gatewayCode(String gatewayCode) {
            this.gatewayCode = gatewayCode;
            return this;
        }

        public Builder invoiceId(UUID invoiceId) {
            this.invoiceId = invoiceId;
            return this;
        }

        public Builder method(PaymentMethod method) {
            this.method = method;
            return this;
        }

        public Builder status(PaymentStatus status) {
            this.status = status;
            return this;
        }

        public Payment build() {
            return new Payment(gatewayCode, invoiceId, method, status);
        }
    }
}