package com.henio.algashop.ordering.domain.model.shared;

public interface Specification<T> {
    boolean isSatisfiedBy(T t);
}
