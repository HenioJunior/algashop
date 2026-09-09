package com.henio.algashop.ordering.domain.model.shared;

import java.util.Optional;

public interface Repository<T extends AggregateRoot<ID>, ID>{
    Optional<T> ofId(ID id);
    void add(T aggregateRoot);
    long count();
    boolean exists(ID id);
}
