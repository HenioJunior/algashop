package com.henio.algashop.ordering.domain.model.entity;

import com.henio.algashop.ordering.domain.model.exception.CustomerArchivedException;
import com.henio.algashop.ordering.domain.model.valueobject.*;
import com.henio.algashop.ordering.domain.model.valueobject.id.CustomerId;

import java.time.OffsetDateTime;
import java.util.Objects;
import java.util.UUID;

public class Customer {

    private static final FullName ANONYMIZED_CUSTOMER_NAME = new FullName("Anonymous", "Customer");

    private static final String ANONYMIZED_EMAIL_DOMAIN =
            "@anonymous.invalid";

    private CustomerId id;
    private FullName fullName;
    private BirthDate birthDate;
    private Email email;
    private Phone phone;
    private Document document;
    private boolean promotionNotificationsAllowed;
    private boolean archived;
    private OffsetDateTime registeredAt;
    private OffsetDateTime archivedAt;
    private LoyaltyPoints loyaltyPoints;
    private Address address;

    private Customer(
            CustomerId id,
            FullName fullName,
            BirthDate birthDate,
            Email email,
            Phone phone,
            Document document,
            boolean promotionNotificationsAllowed,
            boolean archived,
            OffsetDateTime registeredAt,
            OffsetDateTime archivedAt,
            LoyaltyPoints loyaltyPoints,
            Address address
    ) {
        this.id = Objects.requireNonNull(
                id,
                "Customer id is required"
        );

        this.registeredAt = Objects.requireNonNull(
                registeredAt,
                "Registration date is required"
        );

        this.loyaltyPoints = Objects.requireNonNull(
                loyaltyPoints,
                "Loyalty points are required"
        );

        this.address = Objects.requireNonNull(
                address,
                "Address is required"
        );

        this.fullName = fullName;
        this.birthDate = birthDate;
        this.email = email;
        this.phone = phone;
        this.document = document;
        this.promotionNotificationsAllowed =
                promotionNotificationsAllowed;
        this.archived = archived;
        this.archivedAt = archivedAt;
    }

    public static Customer create(
            FullName fullName,
            BirthDate birthDate,
            Email email,
            Phone phone,
            Document document,
            boolean promotionNotificationsAllowed,
            Address address
    ) {
        return new Customer(
                CustomerId.generate(),
                Objects.requireNonNull(fullName, "Full name is required"),
                Objects.requireNonNull(birthDate, "Birth date is required"),
                Objects.requireNonNull(email, "Email is required"),
                Objects.requireNonNull(phone, "Phone is required"),
                Objects.requireNonNull(document, "Document is required"),
                promotionNotificationsAllowed,
                false,
                OffsetDateTime.now(),
                null,
                new LoyaltyPoints(),
                address
        );
    }

    public void addLoyaltyPoints(int points) {
        ensureNotArchived();

        this.loyaltyPoints = this.loyaltyPoints.add(points);
    }

    public void archive() {
        ensureNotArchived();

        this.archived = true;
        this.archivedAt = OffsetDateTime.now();
        this.fullName = ANONYMIZED_CUSTOMER_NAME;
        this.birthDate = null;
        this.email = new Email(UUID.randomUUID() + ANONYMIZED_EMAIL_DOMAIN);
        this.phone = null;
        this.document = null;
        this.promotionNotificationsAllowed = false;
        this.address = this.address().toBuilder()
                .number("Anonymized")
                .complement(null).build();
    }

    public void enablePromotionNotifications() {
        ensureNotArchived();

        this.promotionNotificationsAllowed = true;
    }

    public void disablePromotionNotifications() {
        ensureNotArchived();

        this.promotionNotificationsAllowed = false;
    }

    public void changeName(FullName fullName) {
        ensureNotArchived();

        this.fullName = Objects.requireNonNull(
                fullName,
                "Full name is required"
        );
    }

    public void changeEmail(Email email) {
        ensureNotArchived();

        this.email = Objects.requireNonNull(
                email,
                "Email is required"
        );
    }

    public void changePhone(Phone phone) {
        ensureNotArchived();

        this.phone = Objects.requireNonNull(
                phone,
                "Phone is required"
        );
    }

    public void changeAddress(Address address) {
        ensureNotArchived();

        this.address = Objects.requireNonNull(
                address,
                "Address is required"
        );
    }

    private void ensureNotArchived() {
        if (this.archived) {
            throw new CustomerArchivedException();
        }
    }

    public CustomerId id() {
        return id;
    }

    public FullName fullName() {
        return fullName;
    }

    public BirthDate birthDate() {
        return birthDate;
    }

    public Email email() {
        return email;
    }

    public Phone phone() {
        return phone;
    }

    public Document document() {
        return document;
    }

    public boolean isPromotionNotificationsAllowed() {
        return promotionNotificationsAllowed;
    }

    public boolean isArchived() {
        return archived;
    }

    public OffsetDateTime registeredAt() {
        return registeredAt;
    }

    public OffsetDateTime archivedAt() {
        return archivedAt;
    }

    public LoyaltyPoints loyaltyPoints() {
        return loyaltyPoints;
    }

    public Address address() {
        return address;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }

        if (!(other instanceof Customer customer)) {
            return false;
        }

        return id != null && id.equals(customer.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

}
