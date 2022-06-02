package com.github.dericksm.productservice.repository;

import com.github.dericksm.productservice.model.entity.Product;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface ProductRepository extends ReactiveMongoRepository<Product, String> {

}
