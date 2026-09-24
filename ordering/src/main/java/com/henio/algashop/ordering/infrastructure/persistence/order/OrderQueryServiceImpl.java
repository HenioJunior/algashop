package com.henio.algashop.ordering.infrastructure.persistence.order;

import com.henio.algashop.ordering.application.order.query.OrderDetailOutput;
import com.henio.algashop.ordering.application.order.query.OrderQueryService;
import com.henio.algashop.ordering.application.utility.Mapper;
import com.henio.algashop.ordering.domain.model.order.OrderId;
import com.henio.algashop.ordering.domain.model.order.exception.OrderNotFoundException;
import io.hypersistence.tsid.TSID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrderQueryServiceImpl implements OrderQueryService {

    private final OrderPersistenceEntityRepository repository;
    private final Mapper mapper;

    @Override
    public OrderDetailOutput findById(String orderId) {
        long id = new OrderId(orderId).value().toLong();
        OrderPersistenceEntity entity = repository.findById(id).orElseThrow(
                () -> new OrderNotFoundException(new OrderId(TSID.from(id)))
        );
        return mapper.convert(entity, OrderDetailOutput.class);
    }
}
