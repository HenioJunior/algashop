package com.henio.algashop.ordering.domain.model.shared;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public abstract class AbstractEventSourceEntity implements DomainEventSource{

    private final List<Object> domainEvents = new ArrayList<>();


    protected void publishDomainEvent(Object event) {
        Objects.requireNonNull(event, "Domain event is required");
        domainEvents.add(event);
    }

    @Override
    public List<Object> domainEvents() {
        return Collections.unmodifiableList(domainEvents);
    }

    @Override
    public void clearDomainEvents() {
        domainEvents.clear();
    }
}
