package com.henio.algashop.ordering.infrastructure.persistence.adapter;

import com.henio.algashop.ordering.domain.model.entity.ShoppingCart;
import com.henio.algashop.ordering.domain.model.repository.ShoppingCarts;
import com.henio.algashop.ordering.domain.model.valueobject.id.CustomerId;
import com.henio.algashop.ordering.domain.model.valueobject.id.ShoppingCartId;
import com.henio.algashop.ordering.infrastructure.persistence.AggregateVersionUpdater;
import com.henio.algashop.ordering.infrastructure.persistence.assembler.ShoppingCartPersistenceAssembler;
import com.henio.algashop.ordering.infrastructure.persistence.disassembler.ShoppingCartPersistenceDisassembler;
import com.henio.algashop.ordering.infrastructure.persistence.entity.ShoppingCartPersistenceEntity;
import com.henio.algashop.ordering.infrastructure.persistence.repository.ShoppingCartPersistenceEntityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.Optional;

@Component
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ShoppingCartsPersistenceAdapter implements ShoppingCarts {

    private final ShoppingCartPersistenceEntityRepository persistenceRepository;
    private final ShoppingCartPersistenceAssembler assembler;
    private final ShoppingCartPersistenceDisassembler disassembler;

    @Override
    public Optional<ShoppingCart> ofId(ShoppingCartId shoppingCartId) {
        return persistenceRepository.findById(shoppingCartId.value().toLong())
                .map(disassembler::toDomainEntity);
    }

    @Override
    public boolean exists(ShoppingCartId shoppingCartId) {
        return persistenceRepository.existsById(shoppingCartId.value().toLong());
    }

    @Override
    @Transactional(readOnly = false)
    public void add(ShoppingCart aggregateRoot) {
        Long ShoppingCartId = aggregateRoot.id().value().toLong();

        persistenceRepository.findById(ShoppingCartId)
                .ifPresentOrElse(
                        (persistenceEntity) -> update(aggregateRoot, persistenceEntity),
                        ()-> insert(aggregateRoot)
                );
    }

    @Override
    public long count() {
        return persistenceRepository.count();
    }

    @Override
    @Transactional(readOnly = false)
    public void remove(ShoppingCart shoppingCart) {
        Objects.requireNonNull(shoppingCart, "Shopping cart is required");
        persistenceRepository.deleteById(
                shoppingCart.id().value().toLong()
        );
    }

    @Override
    @Transactional(readOnly = false)
    public void remove(ShoppingCartId shoppingCartId) {
        this.persistenceRepository.deleteById(shoppingCartId.value().toLong());
    }

    @Override
    public Optional<ShoppingCart> ofCustomer(CustomerId customerId) {
        return persistenceRepository.findByCustomer_Id(customerId.value().toLong())
                .map(disassembler::toDomainEntity);
    }

    private void update(ShoppingCart aggregateRoot, ShoppingCartPersistenceEntity entity) {
        checkVersion(aggregateRoot, entity);

        assembler.merge(entity, aggregateRoot);

        persistenceRepository.flush();

        AggregateVersionUpdater.update(
                aggregateRoot,
                entity.getVersion()
        );
    }

    private void insert(ShoppingCart aggregateRoot) {
        ShoppingCartPersistenceEntity entity = assembler.fromDomain(aggregateRoot);
        persistenceRepository.saveAndFlush(entity);
        AggregateVersionUpdater.update(
                aggregateRoot,
                entity.getVersion()
        );
    }

    private void checkVersion(
            ShoppingCart aggregateRoot,
            ShoppingCartPersistenceEntity entity
    ) {
        if (!Objects.equals(
                aggregateRoot.version(),
                entity.getVersion()
        )) {
            throw new ObjectOptimisticLockingFailureException(
                    ShoppingCart.class,
                    aggregateRoot.id()
            );
        }
    }
}
