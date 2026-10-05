package com.henio.algashop.billing.domain.model.invoice;

import java.math.BigDecimal;
import java.util.Objects;

public class LineItem {
    private Integer number;
    private String name;
    private BigDecimal amount;

    protected LineItem() {
    }

    public LineItem(Integer number, String name, BigDecimal amount) {
        this.number = number;
        this.name = name;
        this.amount = amount;
    }

    public Integer getNumber() {
        return number;
    }

    public String getName() {
        return name;
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
}
