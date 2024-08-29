package com.example.posapplicationapis.services.order;

import com.example.posapplicationapis.dto.order.OrderDtoRequest;
import com.example.posapplicationapis.dto.order.OrderDtoResponse;
import com.example.posapplicationapis.dto.order.OrderResponseForReceipt;
import com.example.posapplicationapis.entities.Order;
import jakarta.transaction.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface OrderService {
    OrderDtoResponse createOrder(OrderDtoRequest orderDtoRequest);

    OrderDtoResponse getOrder(Long id);

    List<OrderDtoResponse> getAllOrders();

    OrderDtoResponse updateOrder(Long id, OrderDtoRequest orderDtoRequest);

    @Transactional
    OrderDtoResponse removeProductsFromOrder(Long orderId, List<Long> productIds);

    String deleteOrder(Long id);

    String choosePaymentMethod(Long id, String paymentMethod);

    String chooseDiscount(Long orderId, Long customerId);


    List<OrderDtoResponse> getOrdersByUserId(Long userId);

    String updateOrderStatus(Long orderId,String orderStatus);

    String setPaid(Long orderId);

    OrderResponseForReceipt getProducts(Long orderId);

     List<OrderDtoResponse> getOrdersByDay(LocalDate date);
}
