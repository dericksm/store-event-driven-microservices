package com.github.dericksm.orderservice.eventhandler;

import com.github.dericksm.common.model.ConfirmedOrderEvent;
import com.github.dericksm.common.model.PaymentRejectedEvent;
import com.github.dericksm.orderservice.service.OrderService;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.integration.IntegrationMessageHeaderAccessor;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class PaymentEventListener {

    private final OrderService orderService;
    private final OrderServiceEventProducer orderServiceEventProducer;

    public PaymentEventListener(OrderService orderService,
        OrderServiceEventProducer orderServiceEventProducer) {
        this.orderService = orderService;
        this.orderServiceEventProducer = orderServiceEventProducer;
    }

    @Bean
    public Consumer<Message<PaymentRejectedEvent>> paymentRejected() {
        return message -> {
            PaymentRejectedEvent paymentRejectedEvent = message.getPayload();
            MessageHeaders messageHeaders = message.getHeaders();
            log.info("PaymentRejectedEvent with id '{}' received from bus. topic: {}, partition: {}, offset: {}, deliveryAttempt: {}",
                paymentRejectedEvent.getOrderId(),
                messageHeaders.get(KafkaHeaders.RECEIVED_TOPIC, String.class),
                messageHeaders.get(KafkaHeaders.RECEIVED_PARTITION_ID, Integer.class),
                messageHeaders.get(KafkaHeaders.OFFSET, Long.class),
                messageHeaders.get(IntegrationMessageHeaderAccessor.DELIVERY_ATTEMPT, AtomicInteger.class));

            orderService.rejectOrder(paymentRejectedEvent.getOrderId())
                .doOnNext(orderServiceEventProducer::emitReleaseStockEvent);
        };
    }

    @Bean
    public Consumer<Message<ConfirmedOrderEvent>> paymentConfirmed() {
        return message -> {
            ConfirmedOrderEvent confirmedOrderEvent = message.getPayload();
            MessageHeaders messageHeaders = message.getHeaders();
            log.info("PaymentRejectedEvent with id '{}' received from bus. topic: {}, partition: {}, offset: {}, deliveryAttempt: {}",
                confirmedOrderEvent.getOrderId(),
                messageHeaders.get(KafkaHeaders.RECEIVED_TOPIC, String.class),
                messageHeaders.get(KafkaHeaders.RECEIVED_PARTITION_ID, Integer.class),
                messageHeaders.get(KafkaHeaders.OFFSET, Long.class),
                messageHeaders.get(IntegrationMessageHeaderAccessor.DELIVERY_ATTEMPT, AtomicInteger.class));

            orderService.completeOrder(confirmedOrderEvent.getOrderId());
        };
    }
}
