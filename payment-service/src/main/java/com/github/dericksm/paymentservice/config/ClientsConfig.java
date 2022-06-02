package com.github.dericksm.paymentservice.config;

import com.github.dericksm.clients.ProductServiceClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClient.Builder;

@Configuration
public class ClientsConfig {

    private final WebClient.Builder webClientBuilder;

    public ClientsConfig(Builder webClientBuilder) {
        this.webClientBuilder = webClientBuilder;
    }

    @Bean
    public ProductServiceClient productServiceClient() {
        return new ProductServiceClient(webClientBuilder);
    }

}
