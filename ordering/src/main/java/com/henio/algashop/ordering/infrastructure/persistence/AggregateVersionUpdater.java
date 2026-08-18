package com.henio.algashop.ordering.infrastructure.persistence;

import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Method;

public class AggregateVersionUpdater {

    private AggregateVersionUpdater() {
    }

    public static void update(Object aggregateRoot, Long version) {
        Method method = ReflectionUtils.findMethod(
                aggregateRoot.getClass(),
                "setVersion",
                Long.class
        );

        if (method == null) {
            throw new IllegalStateException(
                    "setVersion method not found in "
                            + aggregateRoot.getClass().getSimpleName()
            );
        }

        ReflectionUtils.makeAccessible(method);

        ReflectionUtils.invokeMethod(
                method,
                aggregateRoot,
                version
        );
    }
}
