package com.henio.algashop.ordering.domain.entity;

import com.henio.algashop.ordering.domain.exception.CustomerArchivedException;
import com.henio.algashop.ordering.domain.valueobject.*;

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
    private Boolean promotionNotificationsAllowed;
    private Boolean archived;
    private OffsetDateTime registeredAt;
    private OffsetDateTime archivedAt;
    private LoyaltyPoints loyaltyPoints;
    private Address address;

    public static Customer createCustomer(
            FullName fullName,
            BirthDate birthDate,
            Email email,
            Phone phone,
            Document document,
            boolean promotionNotificationsAllowed,
            Address address) {

        return new Customer(new CustomerId(), fullName, birthDate, email, phone, document, promotionNotificationsAllowed,
                false, OffsetDateTime.now(), null, new LoyaltyPoints(), address);
    }

    public static Customer existing(
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
        return new Customer(
                id,
                fullName,
                birthDate,
                email,
                phone,
                document,
                promotionNotificationsAllowed,
                archived,
                registeredAt,
                archivedAt,
                loyaltyPoints,
                address
        );
    }

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

        this.fullName = fullName;
        this.birthDate = birthDate;
        this.email = email;
        this.phone = phone;
        this.document = document;

        this.promotionNotificationsAllowed =
                promotionNotificationsAllowed;

        this.registeredAt = Objects.requireNonNull(
                registeredAt,
                "Registration date is required"
        );

        this.archived = archived;
        this.archivedAt = archivedAt;
        this.loyaltyPoints = new LoyaltyPoints();
        this.address = Objects.requireNonNull(
                address,
                "Address is required"
        );;
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
        this.fullName = fullName;
    }

    public void changeEmail(Email email) {
        ensureNotArchived();
        this.email = email;
    }

    public void changePhone(Phone phone) {
        ensureNotArchived();
        this.phone = phone;
    }

    public void changeAddress(Address address) {
        ensureNotArchived();
        this.address = address;
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
