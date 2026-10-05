package com.henio.algashop.billing.domain.model.creditcard;

import java.time.OffsetDateTime;
import java.util.Objects;
import java.util.UUID;

public class CreditCard {

    private UUID id;
    private OffsetDateTime createdAt;
    private UUID customerId;
    private String lastNumbers;
    private String brand;
    private Integer expMonth;
    private Integer expYear;
    private String gatewayCode;

    public UUID getId() {
        return id;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public UUID getCustomerId() {
        return customerId;
    }

    public String getLastNumbers() {
        return lastNumbers;
    }

    public String getBrand() {
        return brand;
    }

    public Integer getExpMonth() {
        return expMonth;
    }

    public Integer getExpYear() {
        return expYear;
    }

    public String getGatewayCode() {
        return gatewayCode;
    }

    public void setGatewayCode(String gatewayCode) {
        this.gatewayCode = gatewayCode;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        CreditCard that = (CreditCard) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
