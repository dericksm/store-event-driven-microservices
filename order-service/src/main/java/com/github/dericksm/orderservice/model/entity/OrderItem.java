package com.github.dericksm.orderservice.model.entity;

import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.Value;

@Value
@Builder
@RequiredArgsConstructor
public class OrderItem {
    private String productId;
    private Integer quantity;
}