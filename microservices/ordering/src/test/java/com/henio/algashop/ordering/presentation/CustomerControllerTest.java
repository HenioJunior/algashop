package com.henio.algashop.ordering.presentation;

import com.henio.algashop.ordering.application.customer.management.CustomerInput;
import com.henio.algashop.ordering.application.customer.management.CustomerManagementApplicationService;
import com.henio.algashop.ordering.application.customer.query.CustomerOutput;
import com.henio.algashop.ordering.application.customer.query.CustomerOutputTestDataBuilder;
import com.henio.algashop.ordering.application.customer.query.CustomerQueryService;
import com.henio.algashop.ordering.domain.model.customer.CustomerId;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.nio.charset.StandardCharsets;

import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;

@WebMvcTest(controllers = CustomerController.class)
class CustomerControllerTest {

    @Autowired
    private WebApplicationContext context;

    @MockitoBean
    private CustomerManagementApplicationService customerManagementApplicationService;

    @MockitoBean
    private CustomerQueryService queryService;

    @BeforeEach
    public void setupAll() {

        RestAssuredMockMvc.mockMvc(MockMvcBuilders.webAppContextSetup(context)
                .defaultResponseCharacterEncoding(StandardCharsets.UTF_8)
                .build()
        );
        RestAssuredMockMvc.enableLoggingOfRequestAndResponseIfValidationFails();
    }

    @Test
    void createCustomerContract() {
        CustomerOutput customerOutput = CustomerOutputTestDataBuilder.existing().build();
        Mockito.when(customerManagementApplicationService.create(any(CustomerInput.class)))
                .thenReturn(new CustomerId());

        Mockito.when(queryService.findById(any(String.class)))
                .thenReturn(customerOutput);

        String jsonInput = """
                {
                  "firstName": "John",
                  "lastName": "Doe",
                  "email": "johndoe@email.com",
                  "document": "12345",
                  "phone": "1191234564",
                  "birthDate": "1991-07-05",
                  "promotionNotificationsAllowed": false,
                  "address": {
                    "street": "Bourbon Street",
                    "number": "2000",
                    "complement": "apt 122",
                    "neighborhood": "North Ville",
                    "city": "Yostfort",
                    "state": "South Carolina",
                    "zipCode": "12321"
                  }
                }
                """;

        RestAssuredMockMvc
                .given()
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(jsonInput)
                .when()
                .post("/api/v1/customers")
                .then()
                .assertThat()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .statusCode(HttpStatus.CREATED.value())
                .body(
                        "id", notNullValue(),
                        "registeredAt", notNullValue(),
                        "firstName", is("John"),
                        "lastName", is("Doe"),
                        "email", is("johndoe@email.com"),
                        "document", is("12345"),
                        "phone", is("1191234564"),
                        "birthDate", is("1991-07-05"),
                        "promotionNotificationsAllowed", is(false),
                        "loyaltyPoints", is(0),
                        "address.street", is("Bourbon Street"),
                        "address.number", is("2000"),
                        "address.complement", is("apt 122"),
                        "address.neighborhood", is("North Ville"),
                        "address.city", is("Yostfort"),
                        "address.state", is("South Carolina"),
                        "address.zipCode", is("12321")
                );
    }
}