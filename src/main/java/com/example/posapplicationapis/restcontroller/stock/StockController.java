package com.example.posapplicationapis.restcontroller.stock;


import com.example.posapplicationapis.dto.stock.StockDtoRequest;
import com.example.posapplicationapis.dto.stock.StockDtoResponse;
import com.example.posapplicationapis.services.stock.StockService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/stock")
public class StockController {
    private StockService stockService;

    public StockController(StockService stockService) {
        this.stockService = stockService;
    }

    @PostMapping(value = "/add-stock")
    public ResponseEntity<StockDtoResponse> createStock(@RequestBody StockDtoRequest stockDtoRequest) {
        return ResponseEntity.ok(stockService.createStock(stockDtoRequest));
    }
    @GetMapping("/get-stock/{id}")
    public ResponseEntity<StockDtoResponse> getStock(@PathVariable Long id) {
        return ResponseEntity.ok(stockService.getStock(id));
    }

    @GetMapping(value = "/get-all-from-stock")
    public ResponseEntity<List<StockDtoResponse>> getAllStocks() {
        return ResponseEntity.ok(stockService.getAllStocks());
    }

    @PutMapping("/update-stock/{id}")
    public ResponseEntity<StockDtoResponse> updateStock(@PathVariable Long id, @RequestBody StockDtoRequest stockDtoRequest) {
        return ResponseEntity.ok(stockService.updateStock(id, stockDtoRequest));
    }

    @DeleteMapping("/delete-stock/{id}")
    public ResponseEntity<Void> deleteStock(@PathVariable Long id) {
        stockService.deleteStock(id);
        return ResponseEntity.noContent().build();
    }
}
