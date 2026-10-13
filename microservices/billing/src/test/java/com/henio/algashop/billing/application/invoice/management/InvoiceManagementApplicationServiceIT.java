package com.henio.algashop.billing.application.invoice.management;

import com.henio.algashop.billing.domain.model.creditcard.CreditCard;
import com.henio.algashop.billing.domain.model.creditcard.CreditCardRepository;
import com.henio.algashop.billing.domain.model.creditcard.CreditCardTestDataBuilder;
import com.henio.algashop.billing.domain.model.invoice.*;
import com.henio.algashop.billing.domain.model.invoice.payment.Payment;
import com.henio.algashop.billing.domain.model.invoice.payment.PaymentGatewayService;
import com.henio.algashop.billing.domain.model.invoice.payment.PaymentRequest;
import com.henio.algashop.billing.domain.model.invoice.payment.PaymentStatus;
import com.henio.algashop.billing.infrastructure.listener.InvoiceEventListener;
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

    @MockitoSpyBean
    private InvoiceEventListener invoiceEventListener;

    @Test
    void shouldPopulateAuditingFields() {
        Invoice invoice = InvoiceTestDataBuilder.anInvoice().build();

        invoiceRepository.saveAndFlush(invoice);

        assertThat(invoice.getVersion()).isNotNull();
        assertThat(invoice.getCreatedAt()).isNotNull();
        assertThat(invoice.getCreatedByUserId()).isNotNull();
    }

    @Test
    public void shouldGenerateInvoiceWithCreditCardAsPayment() {
        CreditCard creditCard = CreditCardTestDataBuilder.aCreditCard().build();
        creditCardRepository.save(creditCard);

        GenerateInvoiceInput input = GenerateInvoiceInputTestDataBuilder.anInput().build();

        input.setPaymentSettings(
                PaymentSettingsInput.builder()
                        .creditCardId(creditCard.getId())
                        .method(PaymentMethod.CREDIT_CARD)
                        .build()
        );

        UUID invoiceId = applicationService.generate(input);

        Invoice invoice = invoiceRepository.findById(invoiceId).orElseThrow();

        assertThat(invoice.getPaymentSettings().getPaymentMethod())
                .isEqualTo(PaymentMethod.CREDIT_CARD);

        assertThat(invoice.getPaymentSettings().getCreditCardId())
                .isEqualTo(creditCard.getId());

        assertThat(invoice.getStatus()).isEqualTo(InvoiceStatus.UNPAID);
        assertThat(invoice.getOrderId()).isEqualTo(input.getOrderId());

        assertThat(invoice.getVersion()).isZero();
        assertThat(invoice.getCreatedAt()).isNotNull();
        assertThat(invoice.getCreatedByUserId()).isNotNull();

        Mockito.verify(invoicingService).issue(any(), any(), any(), any());

        Mockito.verify(invoiceEventListener).listen(any(InvoiceIssuedEvent.class));
    }

    @Test
    public void shouldGenerateInvoiceWithGatewayBalanceAsPayment() {
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

        Mockito.verify(invoiceEventListener).listen(any(InvoicePaidEvent.class));
    }

    @Test
    void shouldGenerateInvoicePaymentAndCancelInvoice() {
        Invoice invoice = InvoiceTestDataBuilder.anInvoice().build();
        invoice.changePaymentSettings(PaymentMethod.GATEWAY_BALANCE, null);
        invoiceRepository.save(invoice);

        Payment payment = Payment.builder()
                .gatewayCode(UUID.randomUUID().toString())
                .invoiceId(invoice.getId())
                .method(invoice.getPaymentSettings().getPaymentMethod())
                .status(PaymentStatus.FAILED)
                .build();

        Mockito.when(paymentGatewayService.capture(Mockito.any(PaymentRequest.class))).thenReturn(payment);

        applicationService.processPayment(invoice.getId());

        Invoice paidInvoice = invoiceRepository.findById(invoice.getId()).orElseThrow();

        assertThat(paidInvoice.isCanceled()).isTrue();

        Mockito.verify(paymentGatewayService).capture(any(PaymentRequest.class));
        Mockito.verify(invoicingService).assignPayment(Mockito.any(Invoice.class), Mockito.any(Payment.class));

        Mockito.verify(invoiceEventListener).listen(any(InvoiceCanceledEvent.class));
    }

}