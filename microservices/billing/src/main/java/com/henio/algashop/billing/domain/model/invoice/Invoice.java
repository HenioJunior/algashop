package com.henio.algashop.billing.domain.model.invoice;

import com.henio.algashop.billing.domain.model.IdGenerator;

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

    private Invoice(UUID id, String orderId, UUID customerId, OffsetDateTime issuedAt, OffsetDateTime paidAt,
                   OffsetDateTime canceledAt, OffsetDateTime expiresAt, BigDecimal totalAmount, InvoiceStatus status,
                   PaymentSettings paymentSettings, Set<LineItem> items, Payer payer, String cancelReason) {
        this.id = id;
        this.orderId = orderId;
        this.customerId = customerId;
        this.issuedAt = issuedAt;
        this.paidAt = paidAt;
        this.canceledAt = canceledAt;
        this.expiresAt = expiresAt;
        this.totalAmount = totalAmount;
        this.status = status;
        this.paymentSettings = paymentSettings;
        this.items = items;
        this.payer = payer;
        this.cancelReason = cancelReason;
    }

    public static Invoice issue(String orderId, UUID customerId, Set<LineItem> items, Payer payer) {

        BigDecimal totalAmount = items.stream().map(LineItem::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add);

        return new Invoice(
                IdGenerator.generateTimeBasedUUID(),
                orderId,
                customerId,
                OffsetDateTime.now(),
                null,
                null,
                OffsetDateTime.now().plusDays(3),
                totalAmount,
                InvoiceStatus.UNPAID,
                null,
                items,
                payer,
                null);
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

    public void changePaymentSettings(PaymentMethod method, UUID creditCardId) {
        this.paymentSettings = PaymentSettings.brandNew(method, creditCardId);
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
