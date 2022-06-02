package com.github.dericksm.orderservice.model.dto.request;

import com.github.dericksm.orderservice.model.entity.Address;
import com.github.dericksm.orderservice.model.entity.OrderItem;
import com.github.dericksm.orderservice.model.entity.PaymentDetails;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@AllArgsConstructor
@Getter
@Setter
public class CreateOrderRequest {

    @NotEmpty
    private String customerId;

    @NotNull
    private Address address;

    @NotEmpty
    private List<OrderItem> orderItems;

//    @NotEmpty
    private PaymentDetails paymentDetails;
}
