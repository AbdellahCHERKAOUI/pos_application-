package com.example.posapplicationapis.dto.orderItem;

import lombok.Data;

import java.util.List;

@Data
public class OrderItemDtoResponse {
    private Long id;
    private Integer quantity;
    private Double price;
    private List<Long> productIds;
}