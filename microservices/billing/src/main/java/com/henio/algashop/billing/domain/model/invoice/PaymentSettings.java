package com.henio.algashop.billing.domain.model.invoice;

import com.henio.algashop.billing.domain.model.IdGenerator;
import com.henio.algashop.billing.shared.DomainException;
import io.micrometer.common.util.StringUtils;

import java.util.Objects;
import java.util.UUID;

public class PaymentSettings {

    private UUID id;
    private UUID creditCardId;
    private String gatewayCode;
    private PaymentMethod method;

    protected PaymentSettings() {
    }

    private PaymentSettings(UUID id, UUID creditCardId, String gatewayCode, PaymentMethod method) {
        this.id = id;
        this.creditCardId = creditCardId;
        this.gatewayCode = gatewayCode;
        this.method = method;
    }

    public static PaymentSettings brandNew(
            PaymentMethod method,
            UUID creditCardId
    ) {
        Objects.requireNonNull(method, "Payment method cannot be null");

        if (method.equals(PaymentMethod.CREDIT_CARD)) {
            Objects.requireNonNull(creditCardId, "Credit card cannot be null for credit card payment");
        }

        return new PaymentSettings(
                IdGenerator.generateTimeBasedUUID(),
                creditCardId,
                null,
                method
        );
    }

    void assignGatewayCode(String gatewayCode) {
        if (StringUtils.isBlank(gatewayCode)) {
            throw new DomainException("Gateway code cannot be blank");
        }
        if (this.gatewayCode != null) {
            throw new DomainException("Gateway code already assigned");
        }
        this.gatewayCode = gatewayCode;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        PaymentSettings that = (PaymentSettings) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
