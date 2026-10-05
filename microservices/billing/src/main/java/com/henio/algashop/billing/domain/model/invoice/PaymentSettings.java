package com.henio.algashop.billing.domain.model.invoice;

import java.util.Objects;
import java.util.UUID;

public class PaymentSettings {

    private UUID id;
    private UUID creditCardId;
    private String gatewayCode;
    private PaymentMethod method;

    protected PaymentSettings() {
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
