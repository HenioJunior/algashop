package com.henio.algashop.ordering.domain.valueobject;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;

class CustomerIdTest {

    @Test
    void givenNoValue_whenCreateCustomerId_shouldGenerateId() {
        CustomerId customerId = new CustomerId();

        assertThat(customerId.value())
                .isNotNull();
    }

    @Test
    void givenValidUuid_whenCreateCustomerId_shouldKeepValue() {
        UUID id = UUID.randomUUID();

        CustomerId customerId = new CustomerId(id);

        assertThat(customerId.value())
                .isEqualTo(id);
    }

    @Test
    void givenNullUuid_whenCreateCustomerId_shouldThrowException() {
        assertThatNullPointerException()
                .isThrownBy(() -> new CustomerId(null));
    }

    @Test
    void givenCustomerId_whenConvertToString_shouldReturnUuid() {
        UUID id = UUID.randomUUID();
        CustomerId customerId = new CustomerId(id);

        assertThat(customerId.toString())
                .isEqualTo(id.toString());
    }
}
