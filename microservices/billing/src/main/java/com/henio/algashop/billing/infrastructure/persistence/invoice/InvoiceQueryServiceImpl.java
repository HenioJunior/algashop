package com.henio.algashop.billing.infrastructure.persistence.invoice;

import com.henio.algashop.billing.application.invoice.query.InvoiceOutput;
import com.henio.algashop.billing.application.invoice.query.InvoiceQueryService;
import com.henio.algashop.billing.application.utility.Mapper;
import com.henio.algashop.billing.domain.model.invoice.Invoice;
import com.henio.algashop.billing.domain.model.invoice.InvoiceNotFoundException;
import com.henio.algashop.billing.domain.model.invoice.InvoiceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class InvoiceQueryServiceImpl implements InvoiceQueryService {

    private final InvoiceRepository invoiceRepository;
    private final Mapper mapper;

    @Override
    public InvoiceOutput findByOrderId(String orderId) {
        Invoice invoice = invoiceRepository.findByOrderId(orderId).orElseThrow(
                () -> new InvoiceNotFoundException("Invoice not found"));

        return mapper.convert(invoice, InvoiceOutput.class);
    }
}
