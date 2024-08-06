package com.example.posapplicationapis.services.orderItem;

import com.example.posapplicationapis.dto.orderItem.OrderItemDtoRequest;
import com.example.posapplicationapis.dto.orderItem.OrderItemDtoResponse;

import java.util.List;

public interface OrderItemService {
    OrderItemDtoResponse createOrderItem(OrderItemDtoRequest requestDto);
    OrderItemDtoResponse getOrderItem(Long id);
    List<OrderItemDtoResponse> getAllOrderItems();
    OrderItemDtoResponse updateOrderItem(Long id, OrderItemDtoRequest requestDto);
    void deleteOrderItem(Long id);
}
