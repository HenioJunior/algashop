package com.henio.algashop.ordering.domain.entity;

import com.henio.algashop.ordering.domain.exception.CustomerArchivedException;
import com.henio.algashop.ordering.domain.exception.DomainException;
import com.henio.algashop.ordering.domain.validation.EmailValidator;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.Locale;
import java.util.Objects;
import java.util.UUID;

import static com.henio.algashop.ordering.domain.exception.CustomerMessages.*;

public class Customer {

    private static final String ANONYMIZED_CUSTOMER_NAME =
            "Anonymized Customer";

    private static final String ANONYMIZED_EMAIL_DOMAIN =
            "@anonymous.invalid";

    private UUID id;
    private String fullName;
    private LocalDate birthDate;
    private String email;
    private String phone;
    private String document;
    private Boolean promotionNotificationsAllowed;
    private Boolean archived;
    private OffsetDateTime registeredAt;
    private OffsetDateTime archivedAt;
    private Integer loyaltyPoints;

    public Customer(
            UUID id,
            String fullName,
            LocalDate birthDate,
            String email,
            String phone,
            String document,
            boolean promotionNotificationsAllowed,
            OffsetDateTime registeredAt
    ) {
        this.id = Objects.requireNonNull(
                id,
                "Customer id is required"
        );

        this.fullName = requireValidFullName(fullName);
        this.birthDate = requireValidBirthDate(birthDate);
        this.email = requireValidEmail(email);
        this.phone = requireValidPhone(phone);
        this.document = requireValidDocument(document);

        this.promotionNotificationsAllowed =
                promotionNotificationsAllowed;

        this.registeredAt = Objects.requireNonNull(
                registeredAt,
                "Registration date is required"
        );

        this.archived = false;
        this.archivedAt = null;
        this.loyaltyPoints = 0;
    }

    public void addLoyaltyPoints(int points) {
        ensureNotArchived();

        if (points <= 0) {
            throw new IllegalArgumentException(LOYALTY_POINTS_MUST_BE_GREATER_THAN_ZERO);
        }

        this.loyaltyPoints = Math.addExact(this.loyaltyPoints, points);
    }

    public void archive() {
        ensureNotArchived();

        this.archived = true;
        this.archivedAt = OffsetDateTime.now();
        this.fullName = ANONYMIZED_CUSTOMER_NAME;
        this.birthDate = null;
        this.email = UUID.randomUUID() + ANONYMIZED_EMAIL_DOMAIN;
        this.phone = null;
        this.document = null;
        this.promotionNotificationsAllowed = false;
    }

    public void enablePromotionNotifications() {
        ensureNotArchived();
        this.promotionNotificationsAllowed = true;
    }

    public void disablePromotionNotifications() {
        ensureNotArchived();
        this.promotionNotificationsAllowed = false;
    }

    public void changeName(String fullName) {
        ensureNotArchived();
        this.fullName = requireValidFullName(fullName);
    }

    public void changeEmail(String email) {
        ensureNotArchived();
        this.email = requireValidEmail(email);
    }

    public void changePhone(String phone) {
        ensureNotArchived();
        this.phone = requireValidPhone(phone);
    }

    private static String requireValidFullName(String fullName) {
        Objects.requireNonNull(fullName, FULL_NAME_IS_REQUIRED);

        String normalizedFullName = fullName.trim();

        if (normalizedFullName.isBlank()) {
            throw new DomainException(FULL_NAME_CANNOT_BE_BLANK);
        }

        return normalizedFullName;
    }

    private static LocalDate requireValidBirthDate(
            LocalDate birthDate
    ) {
        if (birthDate != null && birthDate.isAfter(LocalDate.now())) {
            throw new DomainException(BIRTH_DATE_MUST_BE_IN_PAST);
        }

        return birthDate;
    }

    private static String requireValidEmail(String email) {
        Objects.requireNonNull(email, EMAIL_IS_REQUIRED);

        String normalizedEmail = email
                .trim()
                .toLowerCase(Locale.ROOT);

        if (normalizedEmail.isBlank()) {
            throw new DomainException(EMAIL_CANNOT_BE_BLANK);
        }

        if (!EmailValidator.isValid(normalizedEmail)) {
            throw new DomainException(EMAIL_IS_INVALID);
        }

        return normalizedEmail;
    }

    private static String requireValidPhone(String phone) {
        Objects.requireNonNull(phone, PHONE_IS_REQUIRED);

        String normalizedPhone = phone.trim();

        if (normalizedPhone.isBlank()) {
            throw new DomainException(PHONE_CANNOT_BE_BLANK);
        }

        return normalizedPhone;
    }

    private static String requireValidDocument(String document) {
        Objects.requireNonNull(document,DOCUMENT_IS_REQUIRED);

        String normalizedDocument = document.trim();

        if (normalizedDocument.isBlank()) {
            throw new IllegalArgumentException(DOCUMENT_CANNOT_BE_BLANK);
        }

        return normalizedDocument;
    }

    private void ensureNotArchived() {
        if (this.archived) {
            throw new CustomerArchivedException();
        }
    }

    public UUID id() {
        return id;
    }

    public String fullName() {
        return fullName;
    }

    public LocalDate birthDate() {
        return birthDate;
    }

    public String email() {
        return email;
    }

    public String phone() {
        return phone;
    }

    public String document() {
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

    public int loyaltyPoints() {
        return loyaltyPoints;
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
