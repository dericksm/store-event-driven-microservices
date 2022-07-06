package com.github.dericksm.productservice.controller;

import com.github.dericksm.common.model.ProductResponse;
import com.github.dericksm.productservice.mapper.ProductMapper;
import com.github.dericksm.productservice.model.dto.request.CreateProductRequest;
import com.github.dericksm.productservice.service.ProductService;
import javax.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;
    private final ProductMapper productMapper;

    public ProductController(ProductService productService, ProductMapper productMapper) {
        this.productService = productService;
        this.productMapper = productMapper;
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public Mono<ProductResponse> save(@Valid @RequestBody CreateProductRequest request) {
        return productService.create(productMapper.productRequestToProduct(request))
            .map(product -> productMapper.productToProductResponse(product));
    }
}
