package com.henio.algashop.ordering.infrastructure.shipping.client.rapidex;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.henio.algashop.ordering.domain.model.commons.Money;
import com.henio.algashop.ordering.domain.model.commons.ZipCode;
import com.henio.algashop.ordering.domain.model.order.shipping.ShippingCostService;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

import java.math.BigDecimal;
import java.time.LocalDate;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(properties = {
        "algashop.integrations.shipping.provider=RAPIDEX"
})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
class ShippingCostServiceIT {

    private static final WireMockServer wireMockServer =
            new WireMockServer(
                    wireMockConfig()
                            .dynamicPort()
                            .http2PlainDisabled(true)
            );

    static {
        wireMockServer.start();
    }

    @Autowired
    private ShippingCostService shippingCostService;

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add(
                "algashop.integrations.rapidex.url",
                wireMockServer::baseUrl
        );
    }

    @BeforeEach
    void setUp() {
        wireMockServer.resetAll();
    }

    @AfterAll
    static void tearDown() {
        wireMockServer.stop();
    }

    @Test
    void shouldCalculateShippingCost() {
        wireMockServer.stubFor(
                post(urlEqualTo("/api/delivery-cost"))
                        .willReturn(okJson("""
                                {
                                    "deliveryCost": 25.90,
                                    "estimatedDaysToDeliver": 3
                                }
                                """))
        );

        var request = new ShippingCostService.CalculationRequest(
                new ZipCode("24020"),
                new ZipCode("28610")
        );

        var result = shippingCostService.calculate(request);

        assertThat(result.cost())
                .isEqualTo(new Money(new BigDecimal("25.90")));

        assertThat(result.expectedDate())
                .isEqualTo(LocalDate.now().plusDays(3));

        wireMockServer.verify(
                1, postRequestedFor(urlEqualTo("/api/delivery-cost"))
        );
    }
}