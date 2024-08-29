package com.example.posapplicationapis.dto.orderItem;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class OrderItemDtoResponse {
    private Long id;
    private Double price;
    private Map<Long, Integer> productIds;
}