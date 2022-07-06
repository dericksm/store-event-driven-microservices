package com.github.dericksm.orderservice.controller;

import com.github.dericksm.common.model.OrderResponse;
import com.github.dericksm.orderservice.eventhandler.OrderServiceEventProducer;
import com.github.dericksm.orderservice.mapper.OrderMapper;
import com.github.dericksm.orderservice.model.dto.request.CreateOrderRequest;
import com.github.dericksm.orderservice.service.OrderService;
import javax.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private final OrderMapper orderMapper;
    private final OrderService orderService;
    private final OrderServiceEventProducer orderServiceEventProducer;

    public OrderController(OrderMapper orderMapper, OrderService orderService,
        OrderServiceEventProducer orderServiceEventProducer) {
        this.orderMapper = orderMapper;
        this.orderService = orderService;
        this.orderServiceEventProducer = orderServiceEventProducer;
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public Mono<OrderResponse> initiate(@Valid @RequestBody CreateOrderRequest request) {
        return orderService.create(orderMapper.orderRequestToOrder(request))
            .doOnNext(orderServiceEventProducer::emitCreatedOrderEvent)
            .map(orderMapper::orderToOrderResponse);
    }


}
