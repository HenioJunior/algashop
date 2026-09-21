package com.henio.algashop.ordering.application.shoppingcart.notification;

public interface ShoppingCartNotificationService {
    void notifyShoppingCartCreated(NotifyShoppingCartCreatedInput input);
    void notifyShoppingCartItemAdded(NotifyShoppingCartItemAddedInput input);
    void notifyShoppingCartItemRemoved(NotifyShoppingCartItemRemovedInput input);
    void notifyShoppingCartEmptied(NotifyShoppingCartEmptiedInput input);
}
