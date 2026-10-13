package com.henio.algashop.ordering.domain.model.shared;

import java.util.List;

public interface DomainEventSource {
    List<Object> domainEvents();

    void clearDomainEvents();
}
