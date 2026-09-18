package com.henio.algashop.ordering.application.shoppingcart.notification;

public interface ShoppingNotificationService {
    void notifyShoppingCartCreated(NotifyShoppingCartCreatedInput input);
    void notifyShoppingCartItemAdded(NotifyShoppingCartItemAddedInput input);
    void notifyShoppingCartItemRemoved(NotifyShoppingCartItemRemovedInput input);
    void notifyShoppingCartEmptied(NotifyShoppingCartEmptiedInput input);
}
