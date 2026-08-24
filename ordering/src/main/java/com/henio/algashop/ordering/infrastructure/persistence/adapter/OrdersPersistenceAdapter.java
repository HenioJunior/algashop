package com.henio.algashop.ordering.infrastructure.persistence.adapter;

import com.henio.algashop.ordering.domain.model.entity.Order;
import com.henio.algashop.ordering.domain.model.repository.Orders;
import com.henio.algashop.ordering.domain.model.valueobject.id.CustomerId;
import com.henio.algashop.ordering.domain.model.valueobject.id.OrderId;
import com.henio.algashop.ordering.infrastructure.persistence.AggregateVersionUpdater;
import com.henio.algashop.ordering.infrastructure.persistence.repository.OrderPersistenceEntityRepository;
import com.henio.algashop.ordering.infrastructure.persistence.assembler.OrderPersistenceAssembler;
import com.henio.algashop.ordering.infrastructure.persistence.disassembler.OrderPersistenceDisassembler;
import com.henio.algashop.ordering.infrastructure.persistence.entity.OrderPersistenceEntity;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.time.Year;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Transactional(readOnly = true)
@Component
public class OrdersPersistenceAdapter implements Orders {

    private final OrderPersistenceEntityRepository persistenceRepository;
    private final OrderPersistenceAssembler assembler;
    private final OrderPersistenceDisassembler disassembler;

    public OrdersPersistenceAdapter(OrderPersistenceEntityRepository persistenceRepository, OrderPersistenceAssembler assembler,
                                    OrderPersistenceDisassembler disassembler) {
        this.persistenceRepository = persistenceRepository;
        this.assembler = assembler;
        this.disassembler = disassembler;
    }

    @Override
    public Optional<Order> ofId(OrderId orderId) {
        Optional<OrderPersistenceEntity> possibleEntity = persistenceRepository.findById(orderId.value().toLong());
        return possibleEntity.map(disassembler::toDomain);
    }


    @Transactional(readOnly = false)
    @Override
    public void add(Order aggregateRoot) {
        Objects.requireNonNull(
                aggregateRoot,
                "Order is required");

        long orderId = aggregateRoot.id()
                .value()
                .toLong();

        persistenceRepository.findById(orderId)
                .ifPresentOrElse(
                        entity -> update(aggregateRoot, entity),
                        () -> insert(aggregateRoot));
    }

    @Override
    public long count() {
        return persistenceRepository.count();
    }

    @Override
    public boolean exists(OrderId orderId) {
        return persistenceRepository.existsById(orderId.value().toLong());
    }

    @Override
    public List<Order> placedByCustomerInYear(CustomerId customerId, Year year) {
        OffsetDateTime start = year.atDay(1).atStartOfDay().atOffset(ZoneOffset.UTC);
        OffsetDateTime end = start.plusYears(1).minusNanos(1);

        List<OrderPersistenceEntity> entities = persistenceRepository.findByCustomer_IdAndPlacedAtBetween(
                customerId.value().toLong(), start, end);

        return entities.stream().map(disassembler::toDomain).toList();
    }

    private void update(Order aggregateRoot, OrderPersistenceEntity entity) {
        checkVersion(aggregateRoot, entity);

        assembler.merge(entity, aggregateRoot);

        persistenceRepository.flush();

        AggregateVersionUpdater.update(
                aggregateRoot,
                entity.getVersion()
        );
    }

    private void insert(Order aggregateRoot) {
        OrderPersistenceEntity entity = assembler.fromDomain(aggregateRoot);
        persistenceRepository.saveAndFlush(entity);
        AggregateVersionUpdater.update(
                aggregateRoot,
                entity.getVersion()
        );
    }

    private void checkVersion(
            Order aggregateRoot,
            OrderPersistenceEntity entity
    ) {
        if (!Objects.equals(
                aggregateRoot.version(),
                entity.getVersion()
        )) {
            throw new ObjectOptimisticLockingFailureException(
                    Order.class,
                    aggregateRoot.id()
            );
        }
    }
}
