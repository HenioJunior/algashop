package com.henio.algashop.ordering.domain.model.valueobject;

import com.henio.algashop.ordering.domain.model.exception.DomainException;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;

class DocumentTest {

    @Test
    void givenValidDocument_whenCreateDocument_shouldCreateSuccessfully() {
        Document document = new Document("041.365.698-99");

        assertThat(document.value())
                .isEqualTo("041.365.698-99");
    }

    @Test
    void givenNullDocument_whenCreateDocument_shouldThrowException() {
        assertThatNullPointerException()
                .isThrownBy(() -> new Document(null));
    }

    @Test
    void givenEmptyDocument_whenCreateDocument_shouldThrowException() {
        assertThatExceptionOfType(DomainException.class)
                .isThrownBy(() -> new Document(""));
    }

    @Test
    void givenBlankDocument_whenCreateDocument_shouldThrowException() {
        assertThatExceptionOfType(DomainException.class)
                .isThrownBy(() -> new Document(" "));
    }

    @Test
    void givenDocument_whenConvertToString_shouldReturnDocumentValue() {
        Document document = new Document("041.365.698-99");

        assertThat(document.toString())
                .isEqualTo("041.365.698-99");
    }
}
