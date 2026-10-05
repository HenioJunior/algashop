package com.henio.algashop.billing.domain.model.invoice;

import com.henio.algashop.billing.domain.model.IdGenerator;

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

    public static PaymentSettings brandNew(PaymentMethod method, UUID creditCardId) {
        return new PaymentSettings(
                IdGenerator.generateTimeBasedUUID(),
                creditCardId,
                null,
                method
        );
    }

    void assignGatewayCode(String gatewayCode) {
        this.gatewayCode = gatewayCode;
    }

    public UUID getId() {
        return id;
    }

    public UUID getCreditCardId() {
        return creditCardId;
    }

    public String getGatewayCode() {
        return gatewayCode;
    }

    public PaymentMethod getMethod() {
        return method;
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
