package com.henio.algashop.ordering.infrastructure.notification.shoppingcart;

import com.henio.algashop.ordering.application.shoppingcart.notification.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ShoppingCartNotificationServiceFakeImpl implements ShoppingCartNotificationService {

    @Override
    public void notifyShoppingCartCreated(NotifyShoppingCartCreatedInput input) {
        log.info(
                "Shopping cart {} was created for customer {} at {}",
                input.shoppingCartId(),
                input.customerId(),
                input.createdAt()
        );
    }

    @Override
    public void notifyShoppingCartItemAdded(NotifyShoppingCartItemAddedInput input) {
        log.info(
                "Product {} was added to shopping cart {} for customer {} at {}",
                input.productId(),
                input.shoppingCartId(),
                input.customerId(),
                input.addedAt()
        );
    }

    @Override
    public void notifyShoppingCartItemRemoved(NotifyShoppingCartItemRemovedInput input) {
        log.info(
                "Product {} was removed from shopping cart {} for customer {} at {}",
                input.productId(),
                input.shoppingCartId(),
                input.customerId(),
                input.removedAt()
        );
    }

    @Override
    public void notifyShoppingCartEmptied(NotifyShoppingCartEmptiedInput input) {
        log.info(
                "Shopping cart {} for customer {} was emptied at {}",
                input.shoppingCartId(),
                input.customerId(),
                input.emptiedAt()
        );
    }
}
