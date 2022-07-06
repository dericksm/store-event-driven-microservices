package com.github.dericksm.paymentservice.service;

import com.github.dericksm.clients.ProductServiceClient;
import com.github.dericksm.common.model.OrderEvent;
import com.github.dericksm.common.model.OrderItem;
import com.github.dericksm.paymentservice.exception.PaymentServiceError;
import com.github.dericksm.paymentservice.mapper.AddressMapper;
import com.github.dericksm.paymentservice.mapper.PaymentDetailsMapper;
import com.github.dericksm.paymentservice.model.entity.Invoice;
import com.github.dericksm.paymentservice.model.entity.InvoiceItem;
import com.github.dericksm.paymentservice.repository.InvoiceRepository;
import java.util.List;
import java.util.Random;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class InvoiceService {

    private final InvoiceRepository invoiceRepository;
    private final AddressMapper addressMapper;
    private final PaymentDetailsMapper paymentDetailsMapper;
    private final ProductServiceClient productServiceClient;

    public InvoiceService(InvoiceRepository invoiceRepository, AddressMapper addressMapper,
        PaymentDetailsMapper paymentDetailsMapper, ProductServiceClient productServiceClient) {
        this.invoiceRepository = invoiceRepository;
        this.addressMapper = addressMapper;
        this.paymentDetailsMapper = paymentDetailsMapper;
        this.productServiceClient = productServiceClient;
    }

    public Mono<Invoice> process(OrderEvent orderEvent) {
        Random random = new Random();
        return Flux.fromIterable(orderEvent.getOrderItems())
            .flatMap(this::orderItemToInvoiceItem)
            .collectList()
            .map(invoiceItems -> toInvoice(orderEvent, invoiceItems))
            .flatMap(invoice -> random.nextBoolean() ? invoiceRepository.save(invoice) : Mono.error(new PaymentServiceError(invoice.getOrderId())));
    }

    public Mono<Void> chargeBack(String orderId) {
        return Mono.empty();
    }

    private Mono<InvoiceItem> orderItemToInvoiceItem(OrderItem item) {
        return productServiceClient.findProductItem(item.getProductId())
            .map(product -> new InvoiceItem(product.getId(), product.getName(), item.getQuantity(), product.getPrice()));
    }

    private Invoice toInvoice(OrderEvent orderEvent, List<InvoiceItem> invoiceItems) {
        Invoice invoice = new Invoice();
        List<InvoiceItem> items = invoiceItems;
        invoice.setInvoiceItems(items);
        invoice.setOrderId(orderEvent.getId());
        invoice.setBillingAddress(addressMapper.addressToAddress(orderEvent.getAddress()));
        invoice.setPaymentDetails(paymentDetailsMapper.paymentDetailsToPaymentDetails(orderEvent.getPaymentDetails()));
        invoice.calculateTotal();
        return invoice;

    }
}
