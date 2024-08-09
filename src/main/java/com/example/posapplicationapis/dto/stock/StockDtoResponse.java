package com.example.posapplicationapis.dto.stock;

import lombok.Data;

import java.util.Map;
@Data
public class StockDtoResponse {
    private Long id;
    private Map<Long, Double> ingredientStockMap;
}
