package com.github.dericksm.productservice.mapper;

import com.github.dericksm.productservice.model.dto.request.CreateProductRequest;
import com.github.dericksm.productservice.model.dto.response.ProductResponse;
import com.github.dericksm.productservice.model.entity.Product;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    Product productRequestToProduct(CreateProductRequest createProductRequest);

    ProductResponse productToProductResponse(Product order);
}
