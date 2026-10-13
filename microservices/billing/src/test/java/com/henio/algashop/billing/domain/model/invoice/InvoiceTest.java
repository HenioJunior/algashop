package com.henio.algashop.billing.domain.model.invoice;

import com.henio.algashop.billing.shared.DomainException;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

@SpringBootTest
class InvoiceTest {

    @Test
    void shouldIssueInvoice() {
        Invoice invoice = InvoiceTestDataBuilder.anInvoice()
                .items(
                        InvoiceTestDataBuilder.aLineItem(),
                        InvoiceTestDataBuilder.aLineItemAlt()
                )
                .build();

        assertThat(invoice.getId()).isNotNull();
        assertThat(invoice.getTotalAmount()).isEqualByComparingTo("350.00");
        assertThat(invoice.getStatus()).isEqualTo(InvoiceStatus.UNPAID);
    }

    @Test
    void shouldMarkInvoiceAsPaid() {
        Invoice invoice = InvoiceTestDataBuilder.anInvoice()
                .items(
                        InvoiceTestDataBuilder.aLineItem(),
                        InvoiceTestDataBuilder.aLineItemAlt()
                )
                .build();
        invoice.markAsPaid();

        assertThat(invoice.getId()).isNotNull();
        assertThat(invoice.getTotalAmount()).isEqualByComparingTo("350.00");
        assertThat(invoice.getStatus()).isEqualTo(InvoiceStatus.PAID);
    }

    @Test
    void shouldCancelInvoice() {
        Invoice invoice = InvoiceTestDataBuilder.anInvoice()
                .items(
                        InvoiceTestDataBuilder.aLineItem(),
                        InvoiceTestDataBuilder.aLineItemAlt()
                )
                .build();
        invoice.cancel("Cancelled by user");

        assertThat(invoice.getStatus()).isEqualTo(InvoiceStatus.CANCELED);
    }

    @Test
    void shouldChangePaymentSettings() {
        UUID creditCardId = UUID.randomUUID();
        Invoice invoice = InvoiceTestDataBuilder.anInvoice().build();

        invoice.changePaymentSettings(PaymentMethod.CREDIT_CARD, creditCardId);

        assertThat(invoice.getPaymentSettings().getPaymentMethod())
                .isEqualTo(PaymentMethod.CREDIT_CARD);
        assertThat(invoice.getPaymentSettings().getCreditCardId())
                .isEqualTo(creditCardId);
    }

    @Test
    void shouldAssignPaymentGatewayCode() {
        UUID creditCardId = UUID.randomUUID();
        Invoice invoice = InvoiceTestDataBuilder
                .anInvoice()
                .paymentSettings(PaymentMethod.CREDIT_CARD, creditCardId)
                .build();

        invoice.assignPaymentGatewayCode("algashop");

        assertThat(invoice.getPaymentSettings().getGatewayCode())
                .isEqualTo("algashop");
    }

    @Test
    void shouldNotIssueInvoiceWithEmptyItems() {
        assertThatExceptionOfType(DomainException.class)
                .isThrownBy(() -> InvoiceTestDataBuilder.anInvoice()
                        .withoutItems()
                        .build());
    }

    @Test
    void shouldNotMarkCanceledInvoiceAsPaid() {
        Invoice invoice = InvoiceTestDataBuilder
                .anInvoice()
                .status(InvoiceStatus.CANCELED)
                .build();

        assertThatExceptionOfType(DomainException.class)
                .isThrownBy(invoice::markAsPaid);
    }

    @Test
    void shouldNotCancelAlreadyCanceledInvoice(){
        Invoice invoice = InvoiceTestDataBuilder
                .anInvoice()
                .status(InvoiceStatus.CANCELED)
                .build();

        assertThatExceptionOfType(DomainException.class)
                .isThrownBy(() -> invoice.cancel("Test cancel reason"));
    }

    @Test
    void shouldNotChangePaymentSettingsWhenInvoiceIsPaid() {
        Invoice invoice = InvoiceTestDataBuilder
                .anInvoice()
                .status(InvoiceStatus.PAID)
                .build();

        assertThatExceptionOfType(DomainException.class)
                .isThrownBy(() -> invoice.changePaymentSettings(
                        PaymentMethod.CREDIT_CARD, UUID.randomUUID()));
    }

    @Test
    void shouldNotAssignPaymentGatewayCodeWhenInvoiceIsPaid() {
        Invoice invoice = InvoiceTestDataBuilder
                .anInvoice()
                .status(InvoiceStatus.PAID)
                .build();

        assertThatExceptionOfType(DomainException.class)
                .isThrownBy(() -> invoice.assignPaymentGatewayCode("Gateway Code"));
    }


}