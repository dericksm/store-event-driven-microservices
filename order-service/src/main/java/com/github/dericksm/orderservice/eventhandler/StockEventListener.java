package com.github.dericksm.orderservice.eventhandler;

import com.github.dericksm.common.model.ConfirmedOrderEvent;
import com.github.dericksm.common.model.RejectedOrderEvent;
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
public class StockEventListener {

    private final OrderService orderService;

    public StockEventListener(OrderService orderService) {
        this.orderService = orderService;
    }

    @Bean
    public Consumer<Message<RejectedOrderEvent>> stockReservedRejected() {
        return message -> {
            RejectedOrderEvent rejectedOrderEvent = message.getPayload();
            MessageHeaders messageHeaders = message.getHeaders();
            log.info("RejectedOrderEvent with id '{}' received from bus. topic: {}, partition: {}, offset: {}, deliveryAttempt: {}",
                rejectedOrderEvent.getId(),
                messageHeaders.get(KafkaHeaders.RECEIVED_TOPIC, String.class),
                messageHeaders.get(KafkaHeaders.RECEIVED_PARTITION_ID, Integer.class),
                messageHeaders.get(KafkaHeaders.OFFSET, Long.class),
                messageHeaders.get(IntegrationMessageHeaderAccessor.DELIVERY_ATTEMPT, AtomicInteger.class));

            orderService.rejectOrder(rejectedOrderEvent.getId());
        };
    }

    @Bean
    public Consumer<Message<ConfirmedOrderEvent>> stockReservedConfirmed() {
        return message -> {
            ConfirmedOrderEvent confirmedOrderEvent = message.getPayload();
            MessageHeaders messageHeaders = message.getHeaders();
            log.info("ConfirmedOrderEvent with id '{}' received from bus. topic: {}, partition: {}, offset: {}, deliveryAttempt: {}",
                confirmedOrderEvent.getOrderId(), messageHeaders.get(KafkaHeaders.RECEIVED_TOPIC, String.class),
                messageHeaders.get(KafkaHeaders.RECEIVED_PARTITION_ID, Integer.class), messageHeaders.get(KafkaHeaders.OFFSET, Long.class),
                messageHeaders.get(IntegrationMessageHeaderAccessor.DELIVERY_ATTEMPT, AtomicInteger.class));

            orderService.rejectOrder(confirmedOrderEvent.getOrderId());
        };
    }

}
