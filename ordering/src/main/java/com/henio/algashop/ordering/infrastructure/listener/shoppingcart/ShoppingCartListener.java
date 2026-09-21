package com.henio.algashop.ordering.infrastructure.listener.shoppingcart;

import com.henio.algashop.ordering.application.shoppingcart.notification.*;
import com.henio.algashop.ordering.domain.model.shoppingcart.event.ShoppingCartCreatedEvent;
import com.henio.algashop.ordering.domain.model.shoppingcart.event.ShoppingCartEmptiedEvent;
import com.henio.algashop.ordering.domain.model.shoppingcart.event.ShoppingCartItemAddedEvent;
import com.henio.algashop.ordering.domain.model.shoppingcart.event.ShoppingCartItemRemovedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class ShoppingCartListener {

    private ShoppingCartNotificationService service;

    @EventListener
    public void handleShoppingCartEvent(ShoppingCartCreatedEvent event) {
        log.info("Shopping cart created event received: {}", event);

        NotifyShoppingCartCreatedInput input =
                new NotifyShoppingCartCreatedInput(
                        event.shoppingCartId().toString(),
                        event.customerId().toString(),
                        event.createdAt()
                );

        service.notifyShoppingCartCreated(input);

    }

    @EventListener
    public void handleShoppingCartEvent(ShoppingCartEmptiedEvent event) {
        log.info("Shopping cart emptied event received: {}", event);

        NotifyShoppingCartEmptiedInput input = new NotifyShoppingCartEmptiedInput(
                event.customerId().toString(),
                event.shoppingCartId().toString(),
                event.emptiedAt()
        );

        service.notifyShoppingCartEmptied(input);
    }

    @EventListener
    public void handleShoppingCartEvent(ShoppingCartItemAddedEvent event) {
        log.info("Shopping cart item added event received: {}", event);

        NotifyShoppingCartItemAddedInput input = new NotifyShoppingCartItemAddedInput(
                event.shoppingCartId().toString(),
                event.customerId().toString(),
                event.productId().toString(),
                event.addedAt()
        );

        service.notifyShoppingCartItemAdded(input);
    }

    @EventListener
    public void handleShoppingCartEvent(ShoppingCartItemRemovedEvent event) {
        log.info("Shopping cart item removed event received: {}", event);

        NotifyShoppingCartItemRemovedInput input = new NotifyShoppingCartItemRemovedInput(
                event.shoppingCartId().toString(),
                event.customerId().toString(),
                event.productId().toString(),
                event.removedAt()
        );

        service.notifyShoppingCartItemRemoved(input);
    }






}
