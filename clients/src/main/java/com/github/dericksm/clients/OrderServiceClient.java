package com.github.dericksm.clients;

import com.github.dericksm.common.model.OrderResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

public class OrderServiceClient extends BaseClient {

    private static final String URI = "/orders";

    public OrderServiceClient(WebClient.Builder webClientBuilder) {
        super(webClientBuilder, URI);
    }

    public Mono<OrderResponse> findOrder(String orderId) {
        return webClient
            .get()
            .uri(builder -> builder.path(orderId).build())
            .retrieve()
            .onStatus(HttpStatus::isError, this::mapErrorResponse)
            .bodyToMono(OrderResponse.class);
    }

}
