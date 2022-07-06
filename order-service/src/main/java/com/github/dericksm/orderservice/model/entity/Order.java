package com.github.dericksm.orderservice.model.entity;

import com.github.dericksm.common.model.OrderStatus;
import java.time.Instant;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@Builder
@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
public class Order {

    @Id
    private String id;
    private String customerId;
    private OrderStatus status;
    private List<OrderItem> orderItems;
    private Address address;
    private PaymentDetails paymentDetails;
    private Instant createdAt;
    private Instant updatedAt;

    public Order rejectOrder() {
        this.status = OrderStatus.REJECTED;
        return this;
    }

    public Order reserveOrder() {
        this.status = OrderStatus.RESERVED;
        return this;
    }

    public Order completeOrder() {
        this.status = OrderStatus.COMPLETED;
        return this;
    }
}