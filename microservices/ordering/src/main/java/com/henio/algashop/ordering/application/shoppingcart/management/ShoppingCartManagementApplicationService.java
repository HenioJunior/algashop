package com.henio.algashop.ordering.application.shoppingcart.management;

import com.henio.algashop.ordering.domain.model.commons.Quantity;
import com.henio.algashop.ordering.domain.model.customer.CustomerId;
import com.henio.algashop.ordering.domain.model.product.Product;
import com.henio.algashop.ordering.domain.model.product.ProductCatalogService;
import com.henio.algashop.ordering.domain.model.product.ProductId;
import com.henio.algashop.ordering.domain.model.product.ProductNotFoundException;
import com.henio.algashop.ordering.domain.model.shoppingcart.*;
import com.henio.algashop.ordering.domain.model.shoppingcart.exception.ShoppingCartNotFoundException;
import com.henio.algashop.ordering.domain.model.shoppingcart.service.ShoppingService;
import io.hypersistence.tsid.TSID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ShoppingCartManagementApplicationService {

    private final ShoppingCarts shoppingCarts;
    private final ShoppingService shoppingService;
    private final ProductCatalogService productCatalogService;

    @Transactional
    public void addItem(ShoppingCartItemInput input) {
        Objects.requireNonNull(input, "Input cannot be null");
        ShoppingCartId shoppingCartId = new ShoppingCartId(TSID.from(input.getShoppingCartId()));
        ProductId productId = new ProductId(TSID.from(input.getProductId()));

        ShoppingCart shoppingCart = shoppingCarts.ofId(shoppingCartId)
                .orElseThrow(() -> new ShoppingCartNotFoundException(shoppingCartId));

        Product product = productCatalogService.ofId(productId)
                .orElseThrow(() -> new ProductNotFoundException(productId));

        Quantity quantity = new Quantity(input.getQuantity());

        shoppingCart.addItem(product,quantity);

        shoppingCarts.add(shoppingCart);
    }

    @Transactional
    public ShoppingCartId createNew(String rawCustomerId) {
        Objects.requireNonNull(rawCustomerId, "Customer ID cannot be null");
        CustomerId customerId = new CustomerId(TSID.from(rawCustomerId));

        ShoppingCart shoppingCart = shoppingService.startShopping(customerId);

        shoppingCarts.add(shoppingCart);

        return shoppingCart.id();
    }

    @Transactional
    public void removeItem(String rawShoppingCartId, String rawShoppingCartItemId) {
        Objects.requireNonNull(rawShoppingCartId);
        Objects.requireNonNull(rawShoppingCartItemId);
        ShoppingCartId shoppingCartId = new ShoppingCartId(TSID.from(rawShoppingCartId));
        ShoppingCart shoppingCart = shoppingCarts.ofId(shoppingCartId)
                .orElseThrow(()-> new ShoppingCartNotFoundException(shoppingCartId));
        shoppingCart.removeItem(new ShoppingCartItemId(TSID.from(rawShoppingCartItemId)));
        shoppingCarts.add(shoppingCart);
    }

    @Transactional
    public void empty(String rawShoppingCartId) {
        Objects.requireNonNull(rawShoppingCartId);
        ShoppingCartId shoppingCartId = new ShoppingCartId(TSID.from(rawShoppingCartId));
        ShoppingCart shoppingCart = shoppingCarts.ofId(shoppingCartId)
                .orElseThrow(()-> new ShoppingCartNotFoundException(shoppingCartId));
        shoppingCart.empty();
        shoppingCarts.add(shoppingCart);
    }

    @Transactional
    public void delete(String rawShoppingCartId) {
        Objects.requireNonNull(rawShoppingCartId);
        ShoppingCartId shoppingCartId = new ShoppingCartId(TSID.from(rawShoppingCartId));
        ShoppingCart shoppingCart = shoppingCarts.ofId(shoppingCartId)
                .orElseThrow(()-> new ShoppingCartNotFoundException(shoppingCartId));
        shoppingCarts.remove(shoppingCart);
    }
}
