package com.github.dericksm.orderservice.service;

import com.github.dericksm.orderservice.eventhandler.OrderServiceEventProducer;
import com.github.dericksm.orderservice.exception.OrderServiceException;
import com.github.dericksm.orderservice.model.entity.Order;
import com.github.dericksm.orderservice.repository.OrderRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderServiceEventProducer orderServiceEventProducer;

    public OrderService(OrderRepository orderRepository, OrderServiceEventProducer orderServiceEventProducer) {
        this.orderRepository = orderRepository;
        this.orderServiceEventProducer = orderServiceEventProducer;
    }

    public Mono<Order> create(Order order) {
        return orderRepository.save(order);
    }

    public Mono<Order> findById(String id) {
        return orderRepository.findById(id)
            .switchIfEmpty(Mono.error(new OrderServiceException(String.format("Order with id %s was not found", id))));
    }

    public Mono<Order> rejectOrder(String orderId) {
        return orderRepository.findById(orderId)
            .map(Order::rejectOrder)
            .flatMap(orderRepository::save);
    }

    public Mono<Order> completeOrder(String orderId) {
        return orderRepository.findById(orderId)
            .map(Order::completeOrder)
            .flatMap(orderRepository::save);
    }
}
