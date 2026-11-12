package com.henio.algashop.ordering.presentation.shoppingCart;

import com.henio.algashop.ordering.application.shoppingcart.management.ShoppingCartItemInput;
import com.henio.algashop.ordering.application.shoppingcart.management.ShoppingCartManagementApplicationService;
import com.henio.algashop.ordering.application.shoppingcart.query.ShoppingCartOutput;
import com.henio.algashop.ordering.application.shoppingcart.query.ShoppingCartQueryService;
import com.henio.algashop.ordering.domain.model.shoppingcart.ShoppingCartId;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/shopping-carts")
@RequiredArgsConstructor
public class ShoppingCartController {

    private final ShoppingCartManagementApplicationService managementService;
    private final ShoppingCartQueryService queryService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ShoppingCartOutput create(@RequestBody @Valid ShoppingCartInput input) {
        ShoppingCartId shoppingCartId = managementService.createNew(input.getCustomerId());
        return queryService.findById(shoppingCartId.value().toString());
    }

    @GetMapping("/{shoppingCartId}")
    public ShoppingCartOutput getById(@PathVariable String shoppingCartId) {
        return queryService.findById(shoppingCartId);
    }

    @GetMapping("/{shoppingCartId}/items")
    public ShoppingCartItemListModel getItems(@PathVariable String shoppingCartId) {
        var items = queryService.findById(shoppingCartId).getItems();
        return new ShoppingCartItemListModel(items);
    }

    @DeleteMapping("/{shoppingCartId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable String shoppingCartId) {
        managementService.delete(shoppingCartId);
    }

    @DeleteMapping("/{shoppingCartId}/items")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void empty(@PathVariable String shoppingCartId) {
        managementService.empty(shoppingCartId);
    }

    @PostMapping("/{shoppingCartId}/items")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void addItem(@PathVariable String shoppingCartId,
                        @RequestBody @Valid ShoppingCartItemInput input) {
        input.setShoppingCartId(shoppingCartId);
        managementService.addItem(input);
    }

    @DeleteMapping("/{shoppingCartId}/items/{itemId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeItem(@PathVariable String shoppingCartId,
                           @PathVariable String itemId) {
        managementService.removeItem(shoppingCartId, itemId);
    }
}
