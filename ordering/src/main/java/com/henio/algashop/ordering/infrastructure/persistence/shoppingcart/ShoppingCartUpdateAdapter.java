package com.henio.algashop.ordering.infrastructure.persistence.shoppingcart;

import com.henio.algashop.ordering.domain.model.shoppingcart.ShoppingCartProductAdjustmentService;
import com.henio.algashop.ordering.domain.model.commons.Money;
import com.henio.algashop.ordering.domain.model.product.ProductId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class ShoppingCartUpdateAdapter implements ShoppingCartProductAdjustmentService {

    private final ShoppingCartPersistenceEntityRepository shoppingCartPersistenceEntityRepository;

    @Override
    @Transactional
    public void adjustPrice(ProductId productId, Money updatedPrice) {
        shoppingCartPersistenceEntityRepository.updateItemPrice(productId.value().toLong(), updatedPrice.value());
        shoppingCartPersistenceEntityRepository.recalculateTotalsForCartsWithProduct(productId.value().toLong());

    }

    @Override
    @Transactional
    public void changeAvailability(ProductId productId, boolean available) {
        shoppingCartPersistenceEntityRepository.updateItemAvailability(productId.value().toLong(), available);
    }
}
