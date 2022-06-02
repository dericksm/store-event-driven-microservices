package com.github.dericksm.productservice.service;

import com.github.dericksm.common.model.OrderItem;
import com.github.dericksm.productservice.exception.ProductServiceException;
import com.github.dericksm.productservice.model.entity.Product;
import com.github.dericksm.productservice.repository.ProductRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public Mono<Product> create(Product product) {
        return productRepository.save(product);
    }

    public Mono<Product> findById(String id) {
        return productRepository.findById(id)
            .switchIfEmpty(Mono.error(new ProductServiceException(String.format("Product with id %s was not found", id))));
    }

    public Mono<Void> checkIfHasStock(OrderItem orderItem) {
        var productId = orderItem.getProductId();
        var quantity = orderItem.getQuantity();
        return this.findById(productId).flatMap(product -> product.isStockAvailable(quantity) ? Mono.empty()
            : Mono.error(new ProductServiceException(String.format("Quantity: %d not available for product with id %s", quantity, productId))));
    }

    public Mono<Product> reserveStock(OrderItem orderItem) {
        var productId = orderItem.getProductId();
        var quantity = orderItem.getQuantity();
        return this.findById(productId)
            .map(product -> product.reserveStock(quantity))
            .flatMap(productRepository::save);
    }

    public Mono<Product> releaseStock(OrderItem orderItem) {
        var productId = orderItem.getProductId();
        var quantity = orderItem.getQuantity();
        return this.findById(productId)
            .map(product -> product.releaseStock(quantity))
            .flatMap(productRepository::save);
    }
}
