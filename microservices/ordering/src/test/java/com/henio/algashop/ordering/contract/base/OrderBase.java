package com.henio.algashop.ordering.contract.base;

import com.henio.algashop.ordering.application.checkout.BuyNowApplicationService;
import com.henio.algashop.ordering.application.checkout.BuyNowInput;
import com.henio.algashop.ordering.application.checkout.CheckoutApplicationService;
import com.henio.algashop.ordering.application.checkout.CheckoutInput;
import com.henio.algashop.ordering.application.order.query.OrderDetailOutputTestDataBuilder;
import com.henio.algashop.ordering.application.order.query.OrderFilter;
import com.henio.algashop.ordering.application.order.query.OrderQueryService;
import com.henio.algashop.ordering.application.order.query.OrderSummaryOutputTestDataBuilder;
import com.henio.algashop.ordering.domain.model.order.OrderId;
import com.henio.algashop.ordering.domain.model.order.exception.OrderNotFoundException;
import com.henio.algashop.ordering.presentation.OrderController;
import io.hypersistence.tsid.TSID;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.nio.charset.StandardCharsets;
import java.util.List;

@WebMvcTest(controllers = OrderController.class)
public abstract class OrderBase {

    @Autowired
    private WebApplicationContext context;

    @MockitoBean
    private OrderQueryService orderQueryService;

    @MockitoBean
    private BuyNowApplicationService buyNowApplicationService;

    @MockitoBean
    private CheckoutApplicationService checkoutApplicationService;

    public static final String validOrderId = "01226N0640J7Q";

    public static final String notFoundOrderId = "01226N0693HDH";


    @BeforeEach
    void setup() {
        RestAssuredMockMvc.mockMvc(
                MockMvcBuilders.webAppContextSetup(context)
                        .defaultResponseCharacterEncoding(StandardCharsets.UTF_8)
                        .build()
        );

        RestAssuredMockMvc.enableLoggingOfRequestAndResponseIfValidationFails();

        Mockito.when(orderQueryService.findById(validOrderId))
                .thenReturn(
                        OrderDetailOutputTestDataBuilder
                                .placedOrder(validOrderId)
                                .build()
                );

        Mockito.when(orderQueryService.findById(notFoundOrderId))
                .thenThrow(new OrderNotFoundException(new OrderId(TSID.from(notFoundOrderId))));

        Mockito.when(orderQueryService.filter(Mockito.any(OrderFilter.class)))
                .thenReturn(new PageImpl<>(
                        List.of(OrderSummaryOutputTestDataBuilder
                                .placedOrder()
                                .id(validOrderId)
                                .build())
                ));

        Mockito.when(buyNowApplicationService.buyNow(Mockito.any(BuyNowInput.class)))
                .thenReturn(validOrderId);

        Mockito.when(checkoutApplicationService.checkout(Mockito.any(CheckoutInput.class)))
                .thenReturn(validOrderId);

    }
}
