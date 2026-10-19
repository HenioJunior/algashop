package com.henio.algashop.ordering.presentation;

import com.henio.algashop.ordering.application.commons.AddressData;
import com.henio.algashop.ordering.application.customer.management.CustomerInput;
import com.henio.algashop.ordering.application.customer.management.CustomerManagementApplicationService;
import com.henio.algashop.ordering.application.customer.query.*;
import com.henio.algashop.ordering.domain.model.customer.CustomerId;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.nio.charset.StandardCharsets;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;

@WebMvcTest(controllers = CustomerController.class)
class CustomerControllerTest {

    @Autowired
    private WebApplicationContext context;

    @MockitoBean
    private CustomerManagementApplicationService customerManagementApplicationService;

    @MockitoBean
    private CustomerQueryService customerQueryService;

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
        CustomerId customerId = new CustomerId();

        Mockito.when(customerManagementApplicationService.create(any(CustomerInput.class)))
                .thenReturn(customerId);

        Mockito.when(customerQueryService.findById(any(String.class)))
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
                .header("Location", containsString("/api/v1/customers/" + customerId))
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

    @Test
    void createCustomerErrorContract() {
        String jsonInput = """
                {
                  "firstName": "",
                  "lastName": "",
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
                .contentType(MediaType.APPLICATION_PROBLEM_JSON_VALUE)
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .body(
                        "status", is(HttpStatus.BAD_REQUEST.value()),
                        "type", is("/errors/invalid-fields"),
                        "title", notNullValue(),
                        "detail", notNullValue(),
                        "instance", notNullValue(),
                        "fields", notNullValue()
                );
    }

    @Test
    void findCustomersContract() {
        int sizeLimit = 5;
        int pageNumber = 0;

        CustomerSummaryOutput customer1 = CustomerSummaryOutputTestDataBuilder.existing().build();
        CustomerSummaryOutput customer2 = CustomerSummaryOutputTestDataBuilder.existingAlt1().build();

        Mockito.when(customerQueryService.filter(Mockito.any(CustomerFilter.class)))
                .thenReturn(new PageImpl<>(List.of(customer1, customer2)));

        DateTimeFormatter formatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME;

        RestAssuredMockMvc
                .given()
                .accept(MediaType.APPLICATION_JSON)
                .queryParam("size", sizeLimit)
                .queryParam("page", pageNumber)
                .when()
                .get("/api/v1/customers")
                .then()
                .assertThat()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .statusCode(HttpStatus.OK.value())
                .body(
                        "number", Matchers.equalTo(pageNumber),
                        "size", Matchers.equalTo(2),
                        "totalPages", Matchers.equalTo(1),
                        "totalElements", Matchers.equalTo(2),

                        "content[0].id", Matchers.equalTo(customer1.getId()),
                        "content[0].firstName", Matchers.is(customer1.getFirstName()),
                        "content[0].lastName", Matchers.is(customer1.getLastName()),
                        "content[0].email", Matchers.is(customer1.getEmail()),
                        "content[0].document", Matchers.is(customer1.getDocument()),
                        "content[0].phone", Matchers.is(customer1.getPhone()),
                        "content[0].birthDate", Matchers.is(customer1.getBirthDate().toString()),
                        "content[0].loyaltyPoints", Matchers.is(customer1.getLoyaltyPoints()),
                        "content[0].promotionNotificationsAllowed", Matchers.is(customer1.getPromotionNotificationsAllowed()),
                        "content[0].archived", Matchers.is(customer1.getArchived()),
                        "content[0].registeredAt", Matchers.is(formatter.format(customer1.getRegisteredAt())),

                        "content[1].id", Matchers.equalTo(customer2.getId()),
                        "content[1].firstName", Matchers.is(customer2.getFirstName()),
                        "content[1].lastName", Matchers.is(customer2.getLastName()),
                        "content[1].email", Matchers.is(customer2.getEmail()),
                        "content[1].document", Matchers.is(customer2.getDocument()),
                        "content[1].phone", Matchers.is(customer2.getPhone()),
                        "content[1].birthDate", Matchers.is(customer2.getBirthDate().toString()),
                        "content[1].loyaltyPoints", Matchers.is(customer2.getLoyaltyPoints()),
                        "content[1].promotionNotificationsAllowed", Matchers.is(customer2.getPromotionNotificationsAllowed()),
                        "content[1].archived", Matchers.is(customer2.getArchived()),
                        "content[1].registeredAt", Matchers.is(formatter.format(customer2.getRegisteredAt()))
                );
    }
    
    @Test
    void findByIdCustomer() {
        CustomerOutput customer = CustomerOutputTestDataBuilder.existing().build();

        AddressData address = customer.getAddress();

        Mockito.when(customerQueryService.findById(customer.getId())).thenReturn(customer);

        DateTimeFormatter formatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME;

        RestAssuredMockMvc
                .given()
                    .accept(MediaType.APPLICATION_JSON)
                .when()
                    .get("/api/v1/customers/{customerId}", customer.getId())
                .then()
                    .assertThat()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .statusCode(HttpStatus.OK.value())
                    .body("id", Matchers.is(customer.getId()))
                    .body("firstName", Matchers.is(customer.getFirstName()))
                    .body("lastName", Matchers.is(customer.getLastName()))
                    .body("email", Matchers.is(customer.getEmail()))
                    .body("document", Matchers.is(customer.getDocument()))
                    .body("phone", Matchers.is(customer.getPhone()))
                    .body("birthDate", Matchers.is(customer.getBirthDate().toString()))
                    .body("loyaltyPoints", Matchers.is(customer.getLoyaltyPoints()))
                    .body("promotionNotificationsAllowed", Matchers.is(customer.getPromotionNotificationsAllowed()))
                    .body("archived", Matchers.is(customer.getArchived()))
                    .body("registeredAt", Matchers.is(formatter.format(customer.getRegisteredAt())))
                    .body("address.street", Matchers.is(address.getStreet()))
                    .body("address.number", Matchers.is(address.getNumber()))
                    .body("address.complement", Matchers.is(address.getComplement()))
                    .body("address.neighborhood", Matchers.is(address.getNeighborhood()))
                    .body("address.city", Matchers.is(address.getCity()))
                    .body("address.state", Matchers.is(address.getState()))
                    .body("address.zipCode", Matchers.is(address.getZipCode()));
    }
}
