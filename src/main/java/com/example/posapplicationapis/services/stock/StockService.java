package com.example.posapplicationapis.services.stock;

import com.example.posapplicationapis.dto.stock.StockDtoRequest;
import com.example.posapplicationapis.dto.stock.StockDtoResponse;

import java.util.List;

public interface StockService {
    StockDtoResponse createStock(StockDtoRequest stockDtoRequest);
    StockDtoResponse getStock(Long id);
    List<StockDtoResponse> getAllStocks();
    StockDtoResponse updateStock(Long id, StockDtoRequest stockDtoRequest);
    String deleteStock(Long id);
}
