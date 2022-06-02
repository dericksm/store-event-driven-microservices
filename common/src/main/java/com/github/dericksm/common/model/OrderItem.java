package com.github.dericksm.common.model;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.Value;

@Data
public class OrderItem {
    private String productId;
    private Integer quantity;
}