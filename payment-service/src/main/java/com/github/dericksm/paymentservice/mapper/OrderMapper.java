package com.github.dericksm.paymentservice.mapper;

import com.github.dericksm.clients.ProductServiceClient;
import com.github.dericksm.common.model.OrderEvent;
import com.github.dericksm.common.model.OrderItem;
import com.github.dericksm.common.model.ProductResponse;
import com.github.dericksm.paymentservice.model.entity.Invoice;
import com.github.dericksm.paymentservice.model.entity.InvoiceItem;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

@Component
public class OrderMapper {

    private final ProductServiceClient productServiceClient;
    private final AddressMapper addressMapper;
    private final PaymentDetailsMapper paymentDetailsMapper;

    public OrderMapper(ProductServiceClient productServiceClient, AddressMapper addressMapper,
        PaymentDetailsMapper paymentDetailsMapper) {
        this.productServiceClient = productServiceClient;
        this.addressMapper = addressMapper;
        this.paymentDetailsMapper = paymentDetailsMapper;
    }

    public InvoiceItem orderItemToInvoiceItem(OrderItem item) {
        ProductResponse product = productServiceClient.findProductItem(item.getProductId()).block();
        return new InvoiceItem(product.getId(), product.getName(), item.getQuantity(), product.getPrice());
    }

    public Invoice orderEventToInvoice(OrderEvent orderEvent) {
        Invoice invoice = new Invoice();
        List<InvoiceItem> items = orderEvent.getOrderItems().stream().map(this::orderItemToInvoiceItem).collect(Collectors.toList());
        invoice.setInvoiceItems(items);
        invoice.setOrderId(orderEvent.getId());
        invoice.setBillingAddress(addressMapper.addressToAddress(orderEvent.getAddress()));
        invoice.setPaymentDetails(paymentDetailsMapper.paymentDetailsToPaymentDetails(orderEvent.getPaymentDetails()));
        invoice.calculateTotal();
        return invoice;

    }
}
