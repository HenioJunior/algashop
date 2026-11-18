package com.henio.algashop.ordering.infrastructure.persistence.shoppingcart;

import com.henio.algashop.ordering.application.shoppingcart.query.ShoppingCartOutput;
import com.henio.algashop.ordering.application.shoppingcart.query.ShoppingCartQueryService;
import com.henio.algashop.ordering.application.utility.Mapper;
import com.henio.algashop.ordering.domain.model.customer.CustomerId;
import com.henio.algashop.ordering.domain.model.shoppingcart.ShoppingCartId;
import com.henio.algashop.ordering.domain.model.shoppingcart.exception.ShoppingCartNotFoundException;
import io.hypersistence.tsid.TSID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class ShoppingCartQueryServiceImpl implements ShoppingCartQueryService {

    private final ShoppingCartPersistenceEntityRepository repository;
    private final Mapper mapper;

    @Override
    public ShoppingCartOutput findById(String shoppingCartId) {
        TSID shoppingCartTsid = TSID.from(shoppingCartId);
        Long id = shoppingCartTsid.toLong();

        var shoppingCartPersistenceEntity = repository.findById(id).orElseThrow(
                () -> new ShoppingCartNotFoundException(new ShoppingCartId(shoppingCartTsid)));

        return mapper.convert(shoppingCartPersistenceEntity, ShoppingCartOutput.class);
    }

    @Override
    public ShoppingCartOutput findByCustomerId(UUID customerId) {
        ShoppingCartPersistenceEntity entity = repository.findByCustomer_Id(customerId).orElseThrow(
                () -> new ShoppingCartNotFoundException(new CustomerId(customerId))
        );
        return mapper.convert(entity, ShoppingCartOutput.class);
    }
}
