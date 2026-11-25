package com.henio.algashop.ordering.presentation.customer;

import com.henio.algashop.ordering.infrastructure.persistence.customer.CustomerPersistenceEntityRepository;
import com.henio.algashop.ordering.infrastructure.persistence.customer.CustomerPersistenceEntityTestDataBuilder;
import com.henio.algashop.ordering.utils.AlgaShopResourceUtils;
import io.restassured.RestAssured;
import org.assertj.core.api.Assertions;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class CustomerControllerIT {

    @LocalServerPort
    private int port;

    @Autowired
    private CustomerPersistenceEntityRepository customerRepository;

    private static final UUID validCustomerId = UUID.fromString("6e148bd5-47f6-4022-b9da-07cfaa294f7a");

    @BeforeEach
    void setUp() {
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
        RestAssured.port = port;

        initDatabase();
    }

    private void initDatabase() {
        customerRepository.saveAndFlush(
                CustomerPersistenceEntityTestDataBuilder.aCustomer().id(validCustomerId).build()
        );
    }

    @Test
    void shouldCreateCustomer() {

        String json = AlgaShopResourceUtils
                .readContent("json/create-customer.json");

        UUID createdCustomerId = RestAssured
                .given()
                    .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .body(json)
                .when()
                    .post("/api/v1/customers")
                .then()
                    .assertThat()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .statusCode(HttpStatus.CREATED.value())
                    .body("id", Matchers.not(Matchers.emptyString()))
                    .extract()
                .jsonPath().getUUID("id");

        assertThat(customerRepository.existsById(createdCustomerId)).isTrue();
    }

    @Test
    void shouldNotCreateCustomerWithInvalidData() {

        String json = AlgaShopResourceUtils
                .readContent("json/create-customer-invalid-data.json");

        RestAssured
                .given()
                    .accept(MediaType.APPLICATION_JSON_VALUE)
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .body(json)
                .when()
                    .post("/api/v1/customers")
                .then()
                    .assertThat()
                    .contentType(MediaType.APPLICATION_PROBLEM_JSON_VALUE)
                    .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    public void shouldArchiveCustomer() {
        RestAssured
                .given()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .delete("/api/v1/customers/{customerId}", validCustomerId)
                .then()
                .assertThat()
                .statusCode(HttpStatus.NO_CONTENT.value());

        Assertions.assertThat(customerRepository.existsById(validCustomerId)).isTrue();
        Assertions.assertThat(customerRepository.findById(validCustomerId).orElseThrow().isArchived()).isTrue();
    }

}
