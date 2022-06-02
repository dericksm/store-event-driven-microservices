package com.github.dericksm.clients;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import com.github.dericksm.clients.exception.ServiceApiException;
import com.github.dericksm.clients.model.ApiErrorResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;

public abstract class BaseClient {

    protected final WebClient webClient;

    public BaseClient(WebClient.Builder webClientBuilder, String uri) {
        this.webClient = webClientBuilder
            .baseUrl(uri)
            .defaultHeader(HttpHeaders.CONTENT_TYPE, APPLICATION_JSON_VALUE)
            .defaultHeader(HttpHeaders.ACCEPT, APPLICATION_JSON_VALUE)
            .clientConnector(new ReactorClientHttpConnector(HttpClient.newConnection().compress(true)))
            .build();
    }

    protected Mono<Throwable> mapErrorResponse(ClientResponse response) {
        return response.bodyToMono(ApiErrorResponse.class)
            .flatMap(error -> Mono.error(new ServiceApiException(response.statusCode(), error.getMessage())));
    }


}
