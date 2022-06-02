package com.github.dericksm.clients;

import com.github.dericksm.common.model.ProductResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

public class ProductServiceClient extends BaseClient {

    private static final String URI = "/products";

    public ProductServiceClient(WebClient.Builder webClientBuilder) {
        super(webClientBuilder, URI);
    }

    public Mono<ProductResponse> findProductItem(String productId) {
        return webClient
            .get()
            .uri(builder -> builder.path(productId).build())
            .retrieve()
            .onStatus(HttpStatus::isError, this::mapErrorResponse)
            .bodyToMono(ProductResponse.class);
    }

}
