package com.henio.algashop.ordering.contract.base;

import com.henio.algashop.ordering.application.shoppingcart.management.ShoppingCartManagementApplicationService;
import com.henio.algashop.ordering.application.shoppingcart.query.ShoppingCartOutputTestDataBuilder;
import com.henio.algashop.ordering.application.shoppingcart.query.ShoppingCartQueryService;
import com.henio.algashop.ordering.domain.model.shoppingcart.ShoppingCartId;
import com.henio.algashop.ordering.domain.model.shoppingcart.exception.ShoppingCartNotFoundException;
import com.henio.algashop.ordering.presentation.shoppingCart.ShoppingCartController;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

@WebMvcTest(controllers = ShoppingCartController.class)
public class ShoppingCartBase {

    @Autowired
    private WebApplicationContext context;

    @MockitoBean
    private ShoppingCartManagementApplicationService managementService;

    @MockitoBean
    private ShoppingCartQueryService queryService;

    public static final UUID validShoppingCartId = UUID.fromString("550e8400-e29b-41d4-a716-446655440000");

    public static final UUID validCustomerId = UUID.randomUUID();

    public static final UUID notFoundShoppingCartId = UUID.fromString("550e8400-e29b-41d4-a716-446655440099");

    ShoppingCartId shoppingCartId = new ShoppingCartId(validShoppingCartId);

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
                .thenThrow(new ShoppingCartNotFoundException(new ShoppingCartId(notFoundShoppingCartId)));

        Mockito.when(managementService.createNew(Mockito.any(UUID.class)))
                .thenReturn(shoppingCartId);

    }
}
