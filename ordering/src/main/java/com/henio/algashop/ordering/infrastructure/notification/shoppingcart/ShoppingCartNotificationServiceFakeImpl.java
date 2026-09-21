package com.henio.algashop.ordering.infrastructure.notification.shoppingcart;

import com.henio.algashop.ordering.application.shoppingcart.notification.*;

public class ShoppingCartNotificationServiceFakeImpl implements ShoppingCartNotificationService {

    @Override
    public void notifyShoppingCartCreated(NotifyShoppingCartCreatedInput input) {
        System.out.printf(
                "Shopping cart %s was created for customer %s at %s%n",
                input.shoppingCartId(),
                input.customerId(),
                input.createdAt()
        );
    }

    @Override
    public void notifyShoppingCartItemAdded(NotifyShoppingCartItemAddedInput input) {
        System.out.printf(
                "Product %s was added to shopping cart %s for customer %s at %s%n",
                input.productId(),
                input.shoppingCartId(),
                input.customerId(),
                input.addedAt()
        );
    }

    @Override
    public void notifyShoppingCartItemRemoved(NotifyShoppingCartItemRemovedInput input) {
        System.out.printf(
                "Product %s was removed from shopping cart %s for customer %s at %s%n",
                input.productId(),
                input.shoppingCartId(),
                input.customerId(),
                input.removedAt()
        );
    }

    @Override
    public void notifyShoppingCartEmptied(NotifyShoppingCartEmptiedInput input) {
        System.out.printf(
                "Shopping cart %s for customer %s was emptied at %s%n",
                input.shoppingCartId(),
                input.customerId(),
                input.emptiedAt()
        );
    }
}
