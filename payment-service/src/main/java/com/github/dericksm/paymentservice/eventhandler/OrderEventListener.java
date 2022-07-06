package com.github.dericksm.paymentservice.eventhandler;

import com.github.dericksm.common.model.OrderEvent;
import com.github.dericksm.paymentservice.service.InvoiceService;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.integration.IntegrationMessageHeaderAccessor;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

@Component
@Slf4j
public class OrderEventListener {

    private final InvoiceService invoiceService;
    private final PaymentEventProducer paymentEventProducer;

    public OrderEventListener(InvoiceService invoiceService, PaymentEventProducer paymentEventProducer) {
        this.invoiceService = invoiceService;
        this.paymentEventProducer = paymentEventProducer;
    }

    @Bean
    public Consumer<Message<OrderEvent>> order() {
        return message -> {
            OrderEvent orderEvent = message.getPayload();
            MessageHeaders messageHeaders = message.getHeaders();
            log.info("OrderEvent with id '{}' received from bus. topic: {}, partition: {}, offset: {}, deliveryAttempt: {}", orderEvent.getId(),
                messageHeaders.get(KafkaHeaders.RECEIVED_TOPIC, String.class), messageHeaders.get(KafkaHeaders.RECEIVED_PARTITION_ID, Integer.class),
                messageHeaders.get(KafkaHeaders.OFFSET, Long.class),
                messageHeaders.get(IntegrationMessageHeaderAccessor.DELIVERY_ATTEMPT, AtomicInteger.class));

            Flux.just(orderEvent).map(invoiceService::process).onErrorStop()
                .doOnError(error -> paymentEventProducer.emitPaymentRejectedEvent(orderEvent.getId(), error.getMessage()))
                .doOnComplete(() -> paymentEventProducer.emitPaymentConfirmedEvent(orderEvent.getId())).subscribe();
        };
    }

}
