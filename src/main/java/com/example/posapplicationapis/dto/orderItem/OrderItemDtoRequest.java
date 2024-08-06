package com.example.posapplicationapis.dto.orderItem;

import lombok.Data;

import java.util.List;

@Data
public class OrderItemDtoRequest {
    private Long orderId;
    private Integer quantity;
    private Double price;
    private List<Long> productIds;
}