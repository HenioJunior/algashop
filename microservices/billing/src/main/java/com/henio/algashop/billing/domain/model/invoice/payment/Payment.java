package com.henio.algashop.billing.domain.model.invoice.payment;

import com.henio.algashop.billing.domain.model.FieldValidations;
import com.henio.algashop.billing.domain.model.invoice.PaymentMethod;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.util.Objects;
import java.util.UUID;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Data
@EqualsAndHashCode
@Builder
@Embeddable
public class Payment {

    private String gatewayCode;
    private UUID invoiceId;
    private PaymentMethod method;
    private PaymentStatus status;

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
}