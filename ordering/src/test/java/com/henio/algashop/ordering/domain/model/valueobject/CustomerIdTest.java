package com.henio.algashop.ordering.domain.model.valueobject;

import com.henio.algashop.ordering.domain.model.valueobject.id.CustomerId;
import io.hypersistence.tsid.TSID;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;

class CustomerIdTest {

    @Test
    void givenNoValue_whenCreateCustomerId_shouldGenerateId() {
        CustomerId customerId = CustomerId.generate();

        assertThat(customerId.value())
                .isNotNull();
    }

    @Test
    void shouldNotAcceptNullValue() {
        assertThatNullPointerException()
                .isThrownBy(() -> new CustomerId((TSID) null));
    }

    @Test
    void givenCustomerId_whenConvertToString_shouldReturnUuid() {
        CustomerId customerId = CustomerId.generate();

        assertThat(customerId.toString())
                .isEqualTo(customerId.value().toString());
    }
}
