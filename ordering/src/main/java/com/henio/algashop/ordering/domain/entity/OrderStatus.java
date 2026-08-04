package com.henio.algashop.ordering.domain.entity;

import java.util.Objects;
import java.util.Set;

public enum OrderStatus {

    DRAFT,
    PLACED(DRAFT),
    PAID(PLACED),
    READY(PAID),
    CANCELED(DRAFT, PLACED, PAID, READY);

    private final Set<OrderStatus> previousStatuses;

    OrderStatus(OrderStatus... previousStatuses) {
        this.previousStatuses = Set.of(previousStatuses);
    }

    public boolean canChangeTo(OrderStatus newStatus) {
        Objects.requireNonNull(
                newStatus,
                "New status cannot be null"
        );

        return newStatus.previousStatuses.contains(this);
    }

    public boolean cannotChangeTo(OrderStatus newStatus) {
        return !canChangeTo(newStatus);
    }
}
