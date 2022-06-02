package com.github.dericksm.productservice.eventhandler;

import com.github.dericksm.common.model.ConfirmedOrderEvent;
import com.github.dericksm.common.model.RejectedOrderEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class StockEventProducer {

    private final StreamBridge streamBridge;

    public StockEventProducer(StreamBridge streamBridge) {
        this.streamBridge = streamBridge;
    }

    public void emitRejectedOrderEvent(String orderId, String message) {
        RejectedOrderEvent rejectedOrderEvent = new RejectedOrderEvent();
        rejectedOrderEvent.setId(orderId);
        rejectedOrderEvent.setErrorMessage(message);

        Message<RejectedOrderEvent> event = MessageBuilder
            .withPayload(rejectedOrderEvent)
            .setHeader("partitionKey", rejectedOrderEvent.getId())
            .build();

        streamBridge.send("stockReserveRejected-out-0", event);
    }

    public void emitConfirmedOrderEvent(String orderId) {
        ConfirmedOrderEvent confirmedOrderEvent = new ConfirmedOrderEvent();
        confirmedOrderEvent.setOrderId(orderId);

        Message<ConfirmedOrderEvent> event = MessageBuilder
            .withPayload(confirmedOrderEvent)
            .setHeader("partitionKey", confirmedOrderEvent.getOrderId())
            .build();

        streamBridge.send("stockReserveConfirmed-out-0", event);
    }
}
