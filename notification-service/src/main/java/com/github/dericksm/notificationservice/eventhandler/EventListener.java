package com.github.dericksm.notificationservice.eventhandler;

import com.github.dericksm.common.model.ConfirmedOrderEvent;
import com.github.dericksm.common.model.OrderEvent;
import com.github.dericksm.common.model.PaymentRejectedEvent;
import com.github.dericksm.notificationservice.service.NotificationService;
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
public class EventListener {

    private final NotificationService notificationService;

    public EventListener(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @Bean
    public Consumer<Message<OrderEvent>> order()  {
        return message -> {
            OrderEvent orderEvent = message.getPayload();
            MessageHeaders messageHeaders = message.getHeaders();
            log.info("OrderEvent with id '{}' received from bus. topic: {}, partition: {}, offset: {}, deliveryAttempt: {}", orderEvent.getId(),
                messageHeaders.get(KafkaHeaders.RECEIVED_TOPIC, String.class), messageHeaders.get(KafkaHeaders.RECEIVED_PARTITION_ID, Integer.class),
                messageHeaders.get(KafkaHeaders.OFFSET, Long.class),
                messageHeaders.get(IntegrationMessageHeaderAccessor.DELIVERY_ATTEMPT, AtomicInteger.class));

            notificationService.notifyOrder(orderEvent.getId(), orderEvent.getCustomerId());
        };
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

            notificationService.notifyPayment(paymentRejectedEvent.getOrderId(), paymentRejectedEvent.getMessage());
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

            notificationService.notifyPayment(confirmedOrderEvent.getOrderId(), "Payment confirmed");
        };
    }

}
