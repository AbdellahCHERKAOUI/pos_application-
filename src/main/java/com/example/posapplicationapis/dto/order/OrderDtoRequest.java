package com.example.posapplicationapis.dto.order;

import com.example.posapplicationapis.dto.orderItem.OrderItemDtoRequest;
import com.example.posapplicationapis.enums.OrderStatus;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Data
public class OrderDtoRequest {
    private LocalDateTime date;
    private String receiptNumber;
    private Double total;
    private OrderStatus status;
    private Long paymentId;
    private Long userId;
    private Long customerId;
    private Map<Long, Integer> productQuantities;
    private List<Long> productIdsToRemove;

}
