package com.github.dericksm.paymentservice.service;

import com.github.dericksm.paymentservice.exception.PaymentServiceError;
import com.github.dericksm.paymentservice.model.entity.Invoice;
import com.github.dericksm.paymentservice.repository.InvoiceRepository;
import java.util.Random;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class InvoiceService {

    private final InvoiceRepository invoiceRepository;

    public InvoiceService(InvoiceRepository invoiceRepository) {
        this.invoiceRepository = invoiceRepository;
    }

    public Mono<Invoice> process(Invoice invoice) {
        Random random = new Random();
        if (random.nextBoolean()) {
            return invoiceRepository.save(invoice);
        } else {
            return Mono.error(new PaymentServiceError(invoice.getOrderId()));
        }
    }
}
