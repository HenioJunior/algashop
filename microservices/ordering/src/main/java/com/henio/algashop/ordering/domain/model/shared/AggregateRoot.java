package com.henio.algashop.ordering.domain.model.shared;

public interface AggregateRoot<ID> extends DomainEventSource {
    ID id();
}
