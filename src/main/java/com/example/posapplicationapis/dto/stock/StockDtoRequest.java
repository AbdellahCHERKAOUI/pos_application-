package com.example.posapplicationapis.dto.stock;

import lombok.Data;

import java.util.Map;
import java.util.stream.Collectors;

@Data
public class StockDtoRequest {
    private Map<Long, Double> ingredientStockMap;


}
