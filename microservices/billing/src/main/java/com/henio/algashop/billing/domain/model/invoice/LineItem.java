package com.henio.algashop.billing.domain.model.invoice;

import com.henio.algashop.billing.domain.model.FieldValidations;
import jakarta.persistence.Embeddable;

import java.math.BigDecimal;
import java.util.Objects;

@Embeddable
public class LineItem {
    private Integer number;
    private String name;
    private BigDecimal amount;

    protected LineItem() {
    }

    private LineItem(Integer number, String name, BigDecimal amount) {
        Objects.requireNonNull(number, "Number cannot be null");
        FieldValidations.requiresNonBlank(name);
        Objects.requireNonNull(amount, "Amount cannot be null");

        if(amount.compareTo(BigDecimal.ZERO) <= 0)
            throw new IllegalArgumentException("Amount must be greater than zero");

        if(number <= 0)
            throw new IllegalArgumentException("Number must be greater than zero");

        this.number = number;
        this.name = name;
        this.amount = amount;
    }

    public static Builder builder() {
        return new Builder();
    }

    public BigDecimal getAmount() {
        return amount;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        LineItem lineItem = (LineItem) o;
        return Objects.equals(number, lineItem.number) && Objects.equals(name, lineItem.name) && Objects.equals(amount, lineItem.amount);
    }

    @Override
    public int hashCode() {
        return Objects.hash(number, name, amount);
    }

    public static class Builder {
        private Integer number;
        private String name;
        private BigDecimal amount;

        public Builder number(Integer number) {
            this.number = number;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder amount(BigDecimal amount) {
            this.amount = amount;
            return this;
        }

         public LineItem build() {
            return new LineItem(number, name, amount);
        }
    }
}
