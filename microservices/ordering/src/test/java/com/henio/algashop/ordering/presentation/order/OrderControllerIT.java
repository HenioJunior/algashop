package com.henio.algashop.ordering.presentation.order;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.henio.algashop.ordering.domain.model.customer.Customer;
import com.henio.algashop.ordering.domain.model.customer.CustomerId;
import com.henio.algashop.ordering.domain.model.customer.CustomerTestDataBuilder;
import com.henio.algashop.ordering.domain.model.customer.Customers;
import com.henio.algashop.ordering.domain.model.order.OrderId;
import com.henio.algashop.ordering.domain.model.product.Product;
import com.henio.algashop.ordering.domain.model.product.ProductId;
import com.henio.algashop.ordering.domain.model.product.ProductTestDataBuilder;
import com.henio.algashop.ordering.infrastructure.persistence.order.OrderPersistenceEntityRepository;
import com.henio.algashop.ordering.utils.AlgaShopResourceUtils;
import io.restassured.RestAssured;
import io.restassured.path.json.config.JsonPathConfig;
import io.restassured.response.Response;
import org.assertj.core.api.Assertions;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.UUID;

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;
import static io.restassured.config.JsonConfig.jsonConfig;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class OrderControllerIT {

    @LocalServerPort
    private int port;

    @Autowired
    private Customers customers;

    @Autowired
    private OrderPersistenceEntityRepository orderPersistenceEntityRepositor;

    private WireMockServer wireMockProductCatalog;
    private WireMockServer wireMockRapidex;

    @BeforeEach
    void setup() {
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
        RestAssured.port = port;

        RestAssured.config().jsonConfig(jsonConfig().numberReturnType(JsonPathConfig.NumberReturnType.BIG_DECIMAL));

        wireMockRapidex = new WireMockServer(options()
                .port(8782)
                .usingFilesUnderDirectory("src/test/resources/wiremock/rapidex")
                .globalTemplating(true));

        wireMockProductCatalog = new WireMockServer(options()
                .port(8783)
                .usingFilesUnderDirectory("src/test/resources/wiremock/product-catalog")
                .globalTemplating(true));

        wireMockRapidex.start();
        wireMockProductCatalog.start();
    }

    @AfterEach
    public void after() {
        wireMockRapidex.stop();
        wireMockProductCatalog.stop();
    }

    @Test
    public void shouldCreateOrderUsingProduct() {

        Customer customer = CustomerTestDataBuilder
                .brandNewCustomer()
                .build();

        customers.add(customer);

        UUID customerId = customer.id().value();

        Product product = ProductTestDataBuilder
                .aProduct()
                .id(new ProductId(UUID.fromString(
                        "fffe6ec2-7103-48b3-8e4f-3b58e43fb75a")))
                .build();

        String productId = product.id().toString();

        String json = AlgaShopResourceUtils
                .readContent("json/create-order-with-product.json")
                .replace("${customerId}", customerId.toString())
                .replace("${productId}", productId);
        String createdOrderId = RestAssured
                .given()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType("application/vnd.order-with-product.v1+json")
                .body(json)
                .when()
                .post("/api/v1/orders")
                .then()
                .assertThat()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .statusCode(HttpStatus.CREATED.value())
                .body(
                        "id", Matchers.not(Matchers.emptyString()),
                        "customer.id", Matchers.not(Matchers.emptyString()))
                .extract().jsonPath().getString("id");

        boolean existsOrderId = orderPersistenceEntityRepositor.existsById(new OrderId(createdOrderId).value().toLong());
        Assertions.assertThat(existsOrderId).isTrue();

        Response response = RestAssured
                .given()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType("application/vnd.order-with-product.v1+json")
                .body(json)
                .when()
                .post("/api/v1/orders");

        System.out.println("STATUS: " + response.statusCode());
        System.out.println("CONTENT-TYPE: " + response.contentType());
        System.out.println("BODY: " + response.asPrettyString());

    }

    @Test
    public void shouldNotCreateOrderUsingProductWhenProductAPIIsUnavailable() {
        wireMockProductCatalog.stop();

        Customer customer = CustomerTestDataBuilder
                .brandNewCustomer()
                .build();

        customers.add(customer);

        String customerId = customer.id().toString();

        Product product = ProductTestDataBuilder
                .aProduct()
                .build();

        String productId = product.id().toString();
        String json = AlgaShopResourceUtils
                .readContent("json/create-order-with-product.json")
                .replace("${customerId}", customerId)
                .replace("${productId}", productId);

        RestAssured
                .given()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType("application/vnd.order-with-product.v1+json")
                .body(json)
                .when()
                .post("/api/v1/orders")
                .then()
                .assertThat()
                .contentType(MediaType.APPLICATION_PROBLEM_JSON_VALUE)
                .statusCode(HttpStatus.GATEWAY_TIMEOUT.value());

    }

    @Test
    public void shouldNotCreateOrderUsingProductWhenProductNotExists() {
        String json = AlgaShopResourceUtils.readContent("json/create-order-with-invalid-product.json");

        RestAssured
                .given()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType("application/vnd.order-with-product.v1+json")
                .body(json)
                .when()
                .post("/api/v1/orders")
                .then()
                .assertThat()
                .contentType(MediaType.APPLICATION_PROBLEM_JSON_VALUE)
                .statusCode(HttpStatus.UNPROCESSABLE_CONTENT.value());

    }

    @Test
    public void shouldNotCreateOrderUsingProductWhenCustomerWasNotFound() {

        String customerId = new CustomerId().toString();

        Product product = ProductTestDataBuilder
                .aProduct()
                .build();

        String productId = product.id().toString();

        String json = AlgaShopResourceUtils
                .readContent("json/create-order-with-product.json")
                .replace("${customerId}", customerId)
                .replace("${productId}", productId);
        RestAssured
                .given()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType("application/vnd.order-with-product.v1+json")
                .body(json)
                .when()
                .post("/api/v1/orders")
                .then()
                .assertThat()
                .contentType(MediaType.APPLICATION_PROBLEM_JSON_VALUE)
                .statusCode(HttpStatus.UNPROCESSABLE_CONTENT.value());
    }
}
