package com.henio.algashop.ordering.domain.model.shoppingcart;

import com.henio.algashop.ordering.domain.model.shared.AggregateRoot;
import com.henio.algashop.ordering.domain.model.shared.Repository;

public interface RemoveCapableRepository<T extends AggregateRoot<ID>, ID> extends Repository<T, ID> {
    void remove(T t);
    void remove(ID id);
}
