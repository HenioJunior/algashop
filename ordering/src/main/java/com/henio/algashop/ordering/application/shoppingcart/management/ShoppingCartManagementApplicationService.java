package com.henio.algashop.ordering.application.shoppingcart.management;

import com.henio.algashop.ordering.domain.model.commons.Quantity;
import com.henio.algashop.ordering.domain.model.product.Product;
import com.henio.algashop.ordering.domain.model.product.ProductCatalogService;
import com.henio.algashop.ordering.domain.model.product.ProductId;
import com.henio.algashop.ordering.domain.model.product.ProductNotFoundException;
import com.henio.algashop.ordering.domain.model.shoppingcart.ShoppingCart;
import com.henio.algashop.ordering.domain.model.shoppingcart.ShoppingCartId;
import com.henio.algashop.ordering.domain.model.shoppingcart.ShoppingCartNotFoundException;
import com.henio.algashop.ordering.domain.model.shoppingcart.ShoppingCarts;
import io.hypersistence.tsid.TSID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ShoppingCartManagementApplicationService {

    private final ShoppingCarts shoppingCarts;
    private final ProductCatalogService productCatalogService;

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
}
