package com.henio.algashop.ordering.domain.valueobject;

import com.henio.algashop.ordering.domain.exception.DomainException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class LoyaltyPointsTest {

    @Test
    void shouldGenerateWithValue(){
        LoyaltyPoints loyaltyPoints = new LoyaltyPoints(10);
        Assertions.assertThat(loyaltyPoints.value()).isEqualTo(10);
    }

    @Test
    void shouldAddValue(){
        LoyaltyPoints loyaltyPoints = new LoyaltyPoints(10);
        Assertions.assertThat(loyaltyPoints.add(10).value()).isEqualTo(20);
    }

    @Test
void givenLoyaltyPoints_whenAdditionResultsInNegativeValue_thenShouldThrowException(){
    LoyaltyPoints loyaltyPoints = new LoyaltyPoints(10);
    Assertions.assertThatExceptionOfType(DomainException.class)
            .isThrownBy(() -> loyaltyPoints.add(-15));

    Assertions.assertThat(loyaltyPoints.value()).isEqualTo(10);
}
}