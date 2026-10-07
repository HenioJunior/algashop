package com.henio.algashop.billing.domain.model.invoice;

import com.henio.algashop.billing.domain.model.IdGenerator;
import com.henio.algashop.billing.shared.DomainException;
import io.micrometer.common.util.StringUtils;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.*;

@Entity
public class Invoice {

    @Id
    private UUID id;
    private String orderId;
    private UUID customerId;

    private OffsetDateTime issuedAt;
    private OffsetDateTime paidAt;
    private OffsetDateTime canceledAt;
    private OffsetDateTime expiresAt;

    private BigDecimal totalAmount;

    @Enumerated(EnumType.STRING)
    private InvoiceStatus status;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private PaymentSettings paymentSettings;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(
                    name = "fullName",
                    column = @Column(name = "payer_full_name")
            ),
            @AttributeOverride(
                    name = "document",
                    column = @Column(name = "payer_document")
            ),
            @AttributeOverride(
                    name = "phone",
                    column = @Column(name = "payer_phone")
            ),
            @AttributeOverride(
                    name = "email",
                    column = @Column(name = "payer_email")
            ),
            @AttributeOverride(
                    name = "address.street",
                    column = @Column(name = "payer_address_street")
            ),
            @AttributeOverride(
                    name = "address.number",
                    column = @Column(name = "payer_address_number")
            ),
            @AttributeOverride(
                    name = "address.complement",
                    column = @Column(name = "payer_address_complement")
            ),
            @AttributeOverride(
                    name = "address.neighborhood",
                    column = @Column(name = "payer_address_neighborhood")
            ),
            @AttributeOverride(
                    name = "address.city",
                    column = @Column(name = "payer_address_city")
            ),
            @AttributeOverride(
                    name = "address.state",
                    column = @Column(name = "payer_address_state")
            ),
            @AttributeOverride(
                    name = "address.zipCode",
                    column = @Column(name = "payer_address_zip_code")
            )
    })
    private Payer payer;

    @ElementCollection
    @CollectionTable(name = "invoice_line_items",
            joinColumns = @JoinColumn(name = "invoice_id"))
    private Set<LineItem> items = new HashSet<>();

    private String cancelReason;

    protected Invoice() {
    }

    public Invoice(UUID id, String orderId, UUID customerId, OffsetDateTime issuedAt, OffsetDateTime paidAt,
                   OffsetDateTime canceledAt, OffsetDateTime expiresAt, BigDecimal totalAmount, InvoiceStatus status,
                   PaymentSettings paymentSettings, Payer payer, Set<LineItem> items, String cancelReason
    ) {
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
        this.payer = payer;
        this.items = items;
        this.cancelReason = cancelReason;
    }

    public static Invoice issue(
            String orderId,
            UUID customerId,
            Payer payer,
            Set<LineItem> items
    ) {
        Objects.requireNonNull(customerId, "Customer ID cannot be null");
        Objects.requireNonNull(payer, "Payer cannot be null");
        Objects.requireNonNull(items, "Items cannot be null");

        if (StringUtils.isBlank(orderId)) {
            throw new DomainException("Order ID cannot be empty");
        }

        if (items.isEmpty()) {
            throw new DomainException("Items cannot be empty");
        }

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
                payer,
                items,
                null);
    }

    public UUID getId() {
        return id;
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

    public boolean isUnpaid() {
        return InvoiceStatus.UNPAID.equals(this.getStatus());
    }

    public void markAsPaid() {
        if(!isUnpaid()) {
            throw new DomainException(String.format("Invoice %s with status %s cannot be marked as paid",
                            this.getId(), this.getStatus().toString().toLowerCase()));
        }
        this.paidAt = OffsetDateTime.now();
        this.status = InvoiceStatus.PAID;
    }

    private boolean isCanceled() {
        return InvoiceStatus.CANCELED.equals(this.getStatus());
    }

    public void cancel(String cancelReason) {
        if(isCanceled()) {
            throw new DomainException(String.format("Invoice %s is canceled", this.getId()));
        }
        this.cancelReason = cancelReason;
        this.canceledAt = OffsetDateTime.now();
        this.status = InvoiceStatus.CANCELED;
    }

    public void assignPaymentGatewayCode(String code) {
        if(!isUnpaid()) {
            throw new DomainException(String.format("Invoice %s with status %s cannot be edited",
                            this.getId(), this.getStatus().toString().toLowerCase()));
        }
        this.paymentSettings.assignGatewayCode(code);
    }

    public void changePaymentSettings(PaymentMethod method, UUID creditCardId) {
        if(!isUnpaid()) {
            throw new DomainException(String.format("Invoice %s with status %s cannot be edited",
                    this.getId(), this.getStatus().toString().toLowerCase()));
        }
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
