package com.github.dericksm.paymentservice.eventhandler;

import com.github.dericksm.common.model.PaymentConfirmedEvent;
import com.github.dericksm.common.model.PaymentRejectedEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class PaymentEventProducer {

    private final StreamBridge streamBridge;

    public PaymentEventProducer(StreamBridge streamBridge) {
        this.streamBridge = streamBridge;
    }

    public void emitPaymentRejectedEvent(String orderId, String message) {
        PaymentRejectedEvent paymentRejectedEvent = new PaymentRejectedEvent();
        paymentRejectedEvent.setOrderId(orderId);
        paymentRejectedEvent.setMessage(message);

        Message<PaymentRejectedEvent> event = MessageBuilder
            .withPayload(paymentRejectedEvent)
            .setHeader("partitionKey", paymentRejectedEvent.getOrderId())
            .build();

        streamBridge.send("paymentRejected-out-0", event);
    }

    public void emitPaymentConfirmedEvent(String orderId) {
        PaymentConfirmedEvent paymentConfirmedEvent = new PaymentConfirmedEvent();
        paymentConfirmedEvent.setId(orderId);

        Message<PaymentConfirmedEvent> event = MessageBuilder
            .withPayload(paymentConfirmedEvent)
            .setHeader("partitionKey", paymentConfirmedEvent.getId())
            .build();

        streamBridge.send("paymentConfirmed-out-0", event);
    }
}
