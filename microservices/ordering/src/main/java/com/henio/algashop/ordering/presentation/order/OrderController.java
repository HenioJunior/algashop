package com.henio.algashop.ordering.presentation.order;

import com.henio.algashop.ordering.application.checkout.BuyNowApplicationService;
import com.henio.algashop.ordering.application.checkout.BuyNowInput;
import com.henio.algashop.ordering.application.checkout.CheckoutApplicationService;
import com.henio.algashop.ordering.application.checkout.CheckoutInput;
import com.henio.algashop.ordering.application.order.query.OrderDetailOutput;
import com.henio.algashop.ordering.application.order.query.OrderFilter;
import com.henio.algashop.ordering.application.order.query.OrderQueryService;
import com.henio.algashop.ordering.application.order.query.OrderSummaryOutput;
import com.henio.algashop.ordering.domain.model.customer.exception.CustomerNotFoundException;
import com.henio.algashop.ordering.domain.model.product.ProductNotFoundException;
import com.henio.algashop.ordering.domain.model.shoppingcart.exception.ShoppingCartNotFoundException;
import com.henio.algashop.ordering.presentation.UnprocessableContentException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/api/v1/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderQueryService orderQueryService;
    private final BuyNowApplicationService buyNowApplicationService;
    private final CheckoutApplicationService checkoutApplicationService;

    @GetMapping("/{orderId}")
    public OrderDetailOutput findById(@PathVariable String orderId) {
        return orderQueryService.findById(orderId);
    }

    @GetMapping
    public Page<OrderSummaryOutput> filter(OrderFilter filter) {
        return orderQueryService.filter(filter);
    }

    @PostMapping(consumes = "application/vnd.order-with-product.v1+json")
    @ResponseStatus(HttpStatus.CREATED)
    public OrderDetailOutput createWithProduct(@Valid @RequestBody BuyNowInput input) {
        String orderId;
        try {
            orderId = buyNowApplicationService.buyNow(input);
        } catch (CustomerNotFoundException | ProductNotFoundException e) {
            throw new UnprocessableContentException(e.getMessage());
        }
        return orderQueryService.findById(orderId);
    }

    @PostMapping(consumes = "application/vnd.order-with-shopping-cart.v1+json")
    @ResponseStatus(HttpStatus.CREATED)
    public OrderDetailOutput createWithShoppingCart(@Valid @RequestBody CheckoutInput input) {
        String orderId;
        try {
            orderId = checkoutApplicationService.checkout(input);
        } catch (CustomerNotFoundException | ShoppingCartNotFoundException e) {
            throw new UnprocessableContentException(e.getMessage());
        }
        return orderQueryService.findById(orderId);
    }
}
