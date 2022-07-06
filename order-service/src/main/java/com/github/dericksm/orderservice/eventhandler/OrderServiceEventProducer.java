package com.github.dericksm.orderservice.eventhandler;

import com.github.dericksm.common.model.OrderEvent;
import com.github.dericksm.common.model.ReleaseStockEvent;
import com.github.dericksm.orderservice.mapper.OrderMapper;
import com.github.dericksm.orderservice.model.entity.Order;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class OrderServiceEventProducer {

    private final StreamBridge streamBridge;
    private final OrderMapper orderMapper;

    public OrderServiceEventProducer(StreamBridge streamBridge, OrderMapper orderMapper) {
        this.streamBridge = streamBridge;
        this.orderMapper = orderMapper;
    }

    public void emitCreatedOrderEvent(Order order) {
        OrderEvent orderEvent = orderMapper.orderToOrderEvent(order);

        Message<OrderEvent> message = MessageBuilder.withPayload(orderEvent)
                .setHeader("partitionKey", orderEvent.getId())
                .build();

        log.info("OrderEvent with id '{}' sent to bus.", message.getPayload().getId());
        streamBridge.send("order-out-0", message);
    }

    public void emitReleaseStockEvent(Order order) {
        ReleaseStockEvent releaseStockEvent = orderMapper.orderToReleaseStockEvent(order);

        Message<ReleaseStockEvent> message = MessageBuilder.withPayload(releaseStockEvent)
            .setHeader("partitionKey", releaseStockEvent.getOrderId())
            .build();

        log.info("ReleaseStockEvent with id '{}' sent to bus.", message.getPayload().getOrderId());
        streamBridge.send("releaseStock-out-0", message);
    }
}
