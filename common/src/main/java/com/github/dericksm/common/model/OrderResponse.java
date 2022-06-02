package com.github.dericksm.common.model;

import java.time.Instant;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class OrderResponse {

    private String id;
    private OrderStatus status;
    private List<OrderItem> orderItems;
    private Address address;
    private Instant createdAt;
    private Instant updatedAt;
}
