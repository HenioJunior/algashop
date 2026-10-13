package com.henio.algashop.billing.domain.model.invoice;

import com.henio.algashop.billing.domain.model.invoice.payment.Payment;
import com.henio.algashop.billing.shared.DomainException;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.UUID;

@Service
public class InvoicingService {

    private final InvoiceRepository invoiceRepository;

    public InvoicingService(InvoiceRepository invoiceRepository) {
        this.invoiceRepository = invoiceRepository;
    }

    public Invoice issue(String orderId, UUID customerId, Payer payer, Set<LineItem> items) {
        if (invoiceRepository.existsByOrderId(orderId)) {
            throw new DomainException(String.format("Invoice already exists for order %s", orderId));
        }
        return Invoice.issue(orderId, customerId, payer, items);
    }

    public void assignPayment(Invoice invoice, Payment payment) {
        invoice.assignPaymentGatewayCode(payment.getGatewayCode());
        switch (payment.getStatus()) {
            case FAILED -> invoice.cancel("Payment failed");
            case REFUNDED -> invoice.cancel("Payment refunded");
            case PAID -> invoice.markAsPaid();
        }
    }
}
