package com.github.dericksm.productservice.eventhandler;

import com.github.dericksm.common.model.OrderEvent;
import com.github.dericksm.common.model.ReleaseStockEvent;
import com.github.dericksm.productservice.service.ProductService;
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

    private final ProductService productService;
    private final StockEventProducer stockEventProducer;

    public OrderEventListener(ProductService productService, StockEventProducer stockEventProducer) {
        this.productService = productService;
        this.stockEventProducer = stockEventProducer;
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

            Flux.fromIterable(orderEvent.getOrderItems())
                .flatMap(productService::checkIfHasStock)
                .thenMany(Flux.fromIterable(orderEvent.getOrderItems()))
                .flatMap(productService::reserveStock)
                .onErrorStop()
                .doOnError(error -> stockEventProducer.emitRejectedOrderEvent(orderEvent.getId(), error.getMessage()))
                .doOnComplete(() -> stockEventProducer.emitConfirmedOrderEvent(orderEvent.getId())).subscribe();
        };
    }

    @Bean
    public Consumer<Message<ReleaseStockEvent>> releaseStock() {
        return message -> {
            ReleaseStockEvent releaseStockEvent = message.getPayload();
            MessageHeaders messageHeaders = message.getHeaders();
            log.info("ReleaseStockEvent with id '{}' received from bus. topic: {}, partition: {}, offset: {}, deliveryAttempt: {}", releaseStockEvent.getOrderId(),
                messageHeaders.get(KafkaHeaders.RECEIVED_TOPIC, String.class), messageHeaders.get(KafkaHeaders.RECEIVED_PARTITION_ID, Integer.class),
                messageHeaders.get(KafkaHeaders.OFFSET, Long.class),
                messageHeaders.get(IntegrationMessageHeaderAccessor.DELIVERY_ATTEMPT, AtomicInteger.class));

            Flux.fromIterable(releaseStockEvent.getOrderItems())
                .flatMap(productService::releaseStock);
        };
    }

}
