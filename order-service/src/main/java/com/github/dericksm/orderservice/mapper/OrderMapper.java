package com.github.dericksm.orderservice.mapper;

import com.github.dericksm.common.model.OrderEvent;
import com.github.dericksm.common.model.OrderResponse;
import com.github.dericksm.common.model.ReleaseStockEvent;
import com.github.dericksm.orderservice.model.dto.request.CreateOrderRequest;
import com.github.dericksm.orderservice.model.entity.Order;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface OrderMapper {

    Order orderRequestToOrder(CreateOrderRequest createOrderRequest);

    OrderResponse orderToOrderResponse(Order order);

    OrderEvent orderToOrderEvent(Order order);

    ReleaseStockEvent orderToReleaseStockEvent(Order order);
}
