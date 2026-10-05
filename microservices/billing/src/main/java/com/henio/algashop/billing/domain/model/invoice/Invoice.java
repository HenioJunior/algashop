package com.henio.algashop.billing.domain.model.invoice;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.*;

public class Invoice {

    private UUID id;
    private String orderId;
    private UUID customerId;

    private OffsetDateTime issuedAt;
    private OffsetDateTime paidAt;
    private OffsetDateTime canceledAt;
    private OffsetDateTime expiresAt;

    private BigDecimal totalAmount;

    private InvoiceStatus status;

    private PaymentSettings paymentSettings;

    private Set<LineItem> items = new HashSet<>();

    private Payer payer;

    private String cancelReason;

    protected Invoice() {
    }

    public UUID getId() {
        return id;
    }

    public String getOrderId() {
        return orderId;
    }

    public UUID getCustomerId() {
        return customerId;
    }

    public OffsetDateTime getIssuedAt() {
        return issuedAt;
    }

    public OffsetDateTime getPaidAt() {
        return paidAt;
    }

    public OffsetDateTime getCanceledAt() {
        return canceledAt;
    }

    public OffsetDateTime getExpiresAt() {
        return expiresAt;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public InvoiceStatus getStatus() {
        return status;
    }

    public PaymentSettings getPaymentSettings() {
        return paymentSettings;
    }

    public Set<LineItem> getItems() {
        return Collections.unmodifiableSet(items);
    }

    public Payer getPayer() {
        return payer;
    }

    public String getCancelReason() {
        return cancelReason;
    }

    public void markAsPaid() {

    }

    public void cancel() {

    }

    public void assignPaymentGatewayCode(String code) {

    }

    public void changePaymentSettings(PaymentMethod method, UUID creditCard) {

    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Invoice invoice = (Invoice) o;
        return Objects.equals(id, invoice.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
