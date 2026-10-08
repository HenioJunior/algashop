package com.henio.algashop.billing.domain.model.creditcard;

import com.henio.algashop.billing.domain.model.IdGenerator;
import com.henio.algashop.billing.shared.DomainException;
import io.micrometer.common.util.StringUtils;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;

import java.time.OffsetDateTime;
import java.util.Objects;
import java.util.UUID;

@Getter
@Entity
public class CreditCard {

    @Id
    private UUID id;
    private OffsetDateTime createdAt;
    private UUID customerId;
    private String lastNumbers;
    private String brand;
    private Integer expMonth;
    private Integer expYear;
    private String gatewayCode;

    protected CreditCard() {
    }

    protected CreditCard(UUID id, OffsetDateTime createdAt, UUID customerId, String lastNumbers, String brand,
                         Integer expMonth, Integer expYear, String gatewayCode) {
        this.id = id;
        this.createdAt = createdAt;
        this.customerId = customerId;
        this.lastNumbers = lastNumbers;
        this.brand = brand;
        this.expMonth = expMonth;
        this.expYear = expYear;
        this.gatewayCode = gatewayCode;
    }

    public static CreditCard brandNew(
            UUID customerId,
            String lastNumbers,
            String brand,
            Integer expMonth,
            Integer expYear,
            String gatewayCode
    ) {
        Objects.requireNonNull(customerId, "Customer ID cannot be null");
        Objects.requireNonNull(expMonth, "Expiration month cannot be null");
        Objects.requireNonNull(expYear, "Expiration year cannot be null");

        if (StringUtils.isBlank(lastNumbers)
                || StringUtils.isBlank(brand)
                || StringUtils.isBlank(gatewayCode)) {
            throw new DomainException("Credit card details cannot be blank");
        }

        return new CreditCard(
                IdGenerator.generateTimeBasedUUID(),
                OffsetDateTime.now(),
                customerId,
                lastNumbers,
                brand,
                expMonth,
                expYear,
                gatewayCode
        );
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
