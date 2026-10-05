package com.henio.algashop.billing.domain.model.invoice;

import java.util.Objects;

public class Payer {
    private String fullName;
    private String document;
    private String phone;
    private String email;
    private Address address;

    protected Payer() {
    }

    public Payer(String fullName, String document, String phone, String email, Address address) {
        this.fullName = fullName;
        this.document = document;
        this.phone = phone;
        this.email = email;
        this.address = address;
    }

    public String getFullName() {
        return fullName;
    }

    public String getDocument() {
        return document;
    }

    public String getPhone() {
        return phone;
    }

    public String getEmail() {
        return email;
    }

    public Address getAddress() {
        return address;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Payer payer = (Payer) o;
        return Objects.equals(fullName, payer.fullName) && Objects.equals(document, payer.document) && Objects.equals(phone, payer.phone) && Objects.equals(email, payer.email) && Objects.equals(address, payer.address);
    }

    @Override
    public int hashCode() {
        return Objects.hash(fullName, document, phone, email, address);
    }
}
