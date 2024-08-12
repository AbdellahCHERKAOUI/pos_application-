package com.example.posapplicationapis.services.order;

import com.example.posapplicationapis.dto.order.OrderDtoRequest;
import com.example.posapplicationapis.dto.order.OrderDtoResponse;

import java.util.List;

public interface OrderService {
    OrderDtoResponse createOrder(OrderDtoRequest orderDtoRequest);

    OrderDtoResponse getOrder(Long id);

    List<OrderDtoResponse> getAllOrders();

    OrderDtoResponse updateOrder(Long id, OrderDtoRequest orderDtoRequest);

    String deleteOrder(Long id);

    String choosePaymentMethod(Long id, String paymentMethod);

    String chooseDiscount(Long orderId, Long customerId);
}
