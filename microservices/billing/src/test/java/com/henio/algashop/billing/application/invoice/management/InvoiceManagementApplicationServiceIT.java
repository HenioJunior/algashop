package com.henio.algashop.billing.application.invoice.management;

import com.henio.algashop.billing.domain.model.creditcard.CreditCard;
import com.henio.algashop.billing.domain.model.creditcard.CreditCardRepository;
import com.henio.algashop.billing.domain.model.creditcard.CreditCardTestDataBuilder;
import com.henio.algashop.billing.domain.model.invoice.*;
import com.henio.algashop.billing.domain.model.invoice.payment.Payment;
import com.henio.algashop.billing.domain.model.invoice.payment.PaymentGatewayService;
import com.henio.algashop.billing.domain.model.invoice.payment.PaymentRequest;
import com.henio.algashop.billing.domain.model.invoice.payment.PaymentStatus;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;

@SpringBootTest
@Transactional
class InvoiceManagementApplicationServiceIT {

    @Autowired
    private InvoiceManagementApplicationService applicationService;

    @Autowired
    private InvoiceRepository invoiceRepository;

    @Autowired
    private CreditCardRepository creditCardRepository;

    @MockitoSpyBean
    private InvoicingService invoicingService;

    @MockitoBean
    private PaymentGatewayService paymentGatewayService;

    @Test
    public void shouldGenerateInvoiceWithCreditCardAsPayment() {
        UUID customerId = UUID.randomUUID();
        CreditCard creditCard = CreditCardTestDataBuilder.aCreditCard().build();
        creditCardRepository.saveAndFlush(creditCard);

        GenerateInvoiceInput input = GenerateInvoiceInputTestDataBuilder.anInput().build();

        input.setPaymentSettings(
                PaymentSettingsInput.builder()
                        .creditCardId(creditCard.getId())
                        .method(PaymentMethod.CREDIT_CARD)
                        .build()
        );

        UUID invoiceId = applicationService.generate(input);

        Invoice invoice = invoiceRepository.findById(invoiceId).orElseThrow();

        assertThat(invoice.getStatus()).isEqualTo(InvoiceStatus.UNPAID);
        assertThat(invoice.getOrderId()).isEqualTo(input.getOrderId());

        Mockito.verify(invoicingService).issue(any(), any(), any(), any());
    }

    @Test
    public void shouldGenerateInvoiceWithGatewayBalanceAsPayment() {
        UUID customerId = UUID.randomUUID();
        GenerateInvoiceInput input = GenerateInvoiceInputTestDataBuilder.anInput().build();

        input.setPaymentSettings(
                PaymentSettingsInput.builder()
                        .method(PaymentMethod.GATEWAY_BALANCE)
                        .build()
        );

        UUID invoiceId = applicationService.generate(input);

        Invoice invoice = invoiceRepository.findById(invoiceId).orElseThrow();

        assertThat(invoice.getStatus()).isEqualTo(InvoiceStatus.UNPAID);
        assertThat(invoice.getOrderId()).isEqualTo(input.getOrderId());

        Mockito.verify(invoicingService).issue(any(), any(), any(), any());
    }

    @Test
    void shouldGenerateInvoicePayment() {
        Invoice invoice = InvoiceTestDataBuilder.anInvoice().build();
        invoice.changePaymentSettings(PaymentMethod.GATEWAY_BALANCE, null);
        invoiceRepository.save(invoice);

        Payment payment = Payment.builder()
                .gatewayCode(UUID.randomUUID().toString())
                .invoiceId(invoice.getId())
                .method(invoice.getPaymentSettings().getPaymentMethod())
                .status(PaymentStatus.PAID)
                .build();

        Mockito.when(paymentGatewayService.capture(Mockito.any(PaymentRequest.class))).thenReturn(payment);

        applicationService.processPayment(invoice.getId());

        Invoice paidInvoice = invoiceRepository.findById(invoice.getId()).orElseThrow();

        assertThat(paidInvoice.isPaid()).isTrue();

        Mockito.verify(paymentGatewayService).capture(any(PaymentRequest.class));
        Mockito.verify(invoicingService).assignPayment(Mockito.any(Invoice.class), Mockito.any(Payment.class));

    }

}