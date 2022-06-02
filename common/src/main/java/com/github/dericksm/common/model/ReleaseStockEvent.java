package com.github.dericksm.common.model;

import java.util.List;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ReleaseStockEvent {

    private String orderId;
    private List<OrderItem> orderItems;
}