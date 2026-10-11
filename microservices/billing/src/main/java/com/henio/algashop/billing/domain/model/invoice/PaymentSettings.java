package com.henio.algashop.billing.domain.model.invoice;

import com.henio.algashop.billing.domain.model.IdGenerator;
import com.henio.algashop.billing.shared.DomainException;
import io.micrometer.common.util.StringUtils;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import lombok.*;

import java.util.Objects;
import java.util.UUID;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
public class PaymentSettings {

    @Id
    @EqualsAndHashCode.Include
    private UUID id;
    private UUID creditCardId;
    private String gatewayCode;

    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;

    private PaymentSettings(UUID id, UUID creditCardId, String gatewayCode, PaymentMethod paymentMethod) {
        this.id = id;
        this.creditCardId = creditCardId;
        this.gatewayCode = gatewayCode;
        this.paymentMethod = paymentMethod;
    }

    public static PaymentSettings brandNew(
            PaymentMethod paymentMethod,
            UUID creditCardId
    ) {
        Objects.requireNonNull(paymentMethod, "paymentMethod cannot be null");

        if (paymentMethod.equals(PaymentMethod.CREDIT_CARD)) {
            Objects.requireNonNull(creditCardId, "Credit card cannot be null for credit card payment");
        }

        return new PaymentSettings(
                IdGenerator.generateTimeBasedUUID(),
                creditCardId,
                null,
                paymentMethod
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
}
