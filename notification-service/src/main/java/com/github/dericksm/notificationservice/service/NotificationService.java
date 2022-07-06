package com.github.dericksm.notificationservice.service;

import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class NotificationService {


    public Mono<Void> notifyPayment(String orderId, String message) {
        return Mono.empty();
    }

    public Mono<Void> notifyShipping() {
        return Mono.empty();
    }

    public Mono<Void> notifyOrder(String id, String customerId) {
        return Mono.empty();
    }
}
