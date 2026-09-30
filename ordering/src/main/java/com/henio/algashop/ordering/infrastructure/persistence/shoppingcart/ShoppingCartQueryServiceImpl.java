package com.henio.algashop.ordering.infrastructure.persistence.shoppingcart;

import com.henio.algashop.ordering.application.shoppingcart.query.ShoppingCartOutput;
import com.henio.algashop.ordering.application.shoppingcart.query.ShoppingCartQueryService;
import com.henio.algashop.ordering.application.utility.Mapper;
import com.henio.algashop.ordering.domain.model.shoppingcart.ShoppingCartId;
import com.henio.algashop.ordering.domain.model.shoppingcart.exception.ShoppingCartNotFoundException;
import io.hypersistence.tsid.TSID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ShoppingCartQueryServiceImpl implements ShoppingCartQueryService {

    private final ShoppingCartPersistenceEntityRepository repository;
    private final Mapper mapper;

    @Override
    public ShoppingCartOutput findById(String shoppingCartId) {
        TSID tsid = TSID.from(shoppingCartId);
        Long id = tsid.toLong();

        var shoppingCartPersistenceEntity = repository.findById(id).orElseThrow(
                () -> new ShoppingCartNotFoundException(new ShoppingCartId(TSID.from(shoppingCartId))));

        return mapper.convert(shoppingCartPersistenceEntity, ShoppingCartOutput.class);
    }

    @Override
    public ShoppingCartOutput findByCustomerId(String customerId) {
        return null;
    }
}
