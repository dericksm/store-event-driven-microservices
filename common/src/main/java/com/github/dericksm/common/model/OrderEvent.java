package com.github.dericksm.common.model;

import java.util.List;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OrderEvent {

    private String id;
    private String customerId;
    private List<OrderItem> orderItems;
    private OrderStatus status;
    private Address address;
    private PaymentDetails paymentDetails;
}