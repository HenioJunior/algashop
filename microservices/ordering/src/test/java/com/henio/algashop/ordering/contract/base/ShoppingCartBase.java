package com.henio.algashop.ordering.contract.base;

import com.henio.algashop.ordering.application.shoppingcart.management.ShoppingCartManagementApplicationService;
import com.henio.algashop.ordering.application.shoppingcart.query.ShoppingCartOutputTestDataBuilder;
import com.henio.algashop.ordering.application.shoppingcart.query.ShoppingCartQueryService;
import com.henio.algashop.ordering.domain.model.shoppingcart.ShoppingCartId;
import com.henio.algashop.ordering.domain.model.shoppingcart.exception.ShoppingCartNotFoundException;
import com.henio.algashop.ordering.presentation.ShoppingCartController;
import io.hypersistence.tsid.TSID;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.nio.charset.StandardCharsets;

@WebMvcTest(controllers = ShoppingCartController.class)
public class ShoppingCartBase {

    @Autowired
    private WebApplicationContext context;

    @MockitoBean
    private ShoppingCartManagementApplicationService managementService;

    @MockitoBean
    private ShoppingCartQueryService queryService;

    public static final String validShoppingCartId = "01226N0640J7Q";

    public static final String validCustomerId = "0S66P0865N43G";

    public static final String notFoundShoppingCartId = "0S66P0865N43J";

    ShoppingCartId shoppingCartId = new ShoppingCartId(TSID.from(validShoppingCartId));

    @BeforeEach
    void setUp() {
        RestAssuredMockMvc.mockMvc(
                MockMvcBuilders.webAppContextSetup(context)
                        .defaultResponseCharacterEncoding(StandardCharsets.UTF_8)
                        .build()
        );

        RestAssuredMockMvc.enableLoggingOfRequestAndResponseIfValidationFails();

        Mockito.when(queryService.findById(validShoppingCartId))
                .thenReturn(
                        ShoppingCartOutputTestDataBuilder
                                .aShoppingCart()
                                .id(validShoppingCartId)
                                .customerId(validCustomerId)
                                .build());

        Mockito.when(queryService.findById(notFoundShoppingCartId))
                .thenThrow(new ShoppingCartNotFoundException(new ShoppingCartId(TSID.from(notFoundShoppingCartId))));

        Mockito.when(managementService.createNew(Mockito.any(String.class)))
                .thenReturn(shoppingCartId);

    }
}
