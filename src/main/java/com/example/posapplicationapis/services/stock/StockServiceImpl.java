package com.example.posapplicationapis.services.stock;

import com.example.posapplicationapis.dto.stock.StockDtoRequest;
import com.example.posapplicationapis.dto.stock.StockDtoResponse;
import com.example.posapplicationapis.entities.Ingredient;
import com.example.posapplicationapis.entities.Stock;
import com.example.posapplicationapis.repositories.IngredientRepository;
import com.example.posapplicationapis.repositories.StockRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class StockServiceImpl implements StockService{

    private StockRepository stockRepository;
    private IngredientRepository ingredientRepository;
    private ModelMapper modelMapper;

    public StockServiceImpl(StockRepository stockRepository, IngredientRepository ingredientRepository, ModelMapper modelMapper) {
        this.stockRepository = stockRepository;
        this.ingredientRepository = ingredientRepository;
        this.modelMapper = modelMapper;
    }

    /*@Override
    public StockDtoResponse createStock(StockDtoRequest stockDtoRequest) {
        Stock stock = new Stock();
        Map<Ingredient, Double> ingredientStock = stockDtoRequest.getIngredientStockMap().entrySet().stream()
                .collect(Collectors.toMap(
                        entry -> ingredientRepository.findById(entry.getKey())
                                .orElseThrow(() -> new RuntimeException("Ingredient not found")),
                        Map.Entry::getValue
                ));
        stock.setIngredientStockMap(ingredientStock);

        Stock savedStock = stockRepository.save(stock);
        return modelMapper.map(savedStock, StockDtoResponse.class);
    }*/
    @Override
    public StockDtoResponse createStock(StockDtoRequest stockDtoRequest) {
        // Map StockDtoRequest to Stock entity
        Stock stock = mapToEntity(stockDtoRequest);

        // Retrieve all stocks from the repository
        List<Stock> stocks = stockRepository.findAll();
        Stock savedStock;

        if (stocks.isEmpty()) {
            // If no stocks exist, save the new stock
            savedStock = stockRepository.save(stock);
        } else {
            // If stocks exist, retrieve the first one (assuming you only have one stock to work with)
            Stock stockWork = stocks.get(0);

            // Convert existing Map<Ingredient, Double> to Map<Long, Double>
            Map<Long, Double> existingIngredientStockMap = new HashMap<>();
            for (Map.Entry<Ingredient, Double> entry : stockWork.getIngredientStockMap().entrySet()) {
                existingIngredientStockMap.put(entry.getKey().getId(), entry.getValue());
            }

            // Update or add quantities based on the ingredientStockMap from the request
            for (Map.Entry<Long, Double> entry : stockDtoRequest.getIngredientStockMap().entrySet()) {
                Long ingredientId = entry.getKey();
                Double quantity = entry.getValue();

                // Check if the ingredient already exists in the stock
                if (existingIngredientStockMap.containsKey(ingredientId)) {
                    // Update the quantity
                    Double existingQuantity = existingIngredientStockMap.get(ingredientId);
                    existingIngredientStockMap.put(ingredientId,quantity);
                } else {
                    // Add new ingredient with its quantity
                    existingIngredientStockMap.put(ingredientId, quantity);
                }
            }

            // Convert Map<Long, Double> back to Map<Ingredient, Double>
            Map<Ingredient, Double> updatedIngredientStockMap = new HashMap<>();
            for (Map.Entry<Long, Double> entry : existingIngredientStockMap.entrySet()) {
                Ingredient ingredient = findIngredientById(entry.getKey()); // Assume you have a method to find Ingredient by ID
                updatedIngredientStockMap.put(ingredient, entry.getValue());
            }

            // Set the updated map back to the stock entity
            stockWork.setIngredientStockMap(updatedIngredientStockMap);

            // Save the updated stock
            savedStock = stockRepository.save(stockWork);
        }

        // Map the saved stock to StockDtoResponse and return it
        return mapToDto(savedStock);
    }

    // Helper method to find Ingredient by ID (implement this as needed)
    private Ingredient findIngredientById(Long id) {
        // Implement this method to find and return the Ingredient by its ID
        return ingredientRepository.findById(id).orElseThrow(() -> new RuntimeException("Ingredient not found"));
    }



    @Override
    public StockDtoResponse getStock(Long id) {
        Stock stock = stockRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Stock not found"));
        return mapToDto(stock);
    }

    @Override
    public List<StockDtoResponse> getAllStocks() {
        return stockRepository.findAll().stream()
                .map(this::mapToDto)  // Use the mapToDto method for each Stock entity
                .collect(Collectors.toList());
    }

    @Override
    public StockDtoResponse updateStock(Long id, StockDtoRequest stockDtoRequest) {
        Stock stock = stockRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Stock not found"));

        Map<Ingredient, Double> ingredientStock = stockDtoRequest.getIngredientStockMap().entrySet().stream()
                .collect(Collectors.toMap(
                        entry -> ingredientRepository.findById(entry.getKey())
                                .orElseThrow(() -> new RuntimeException("Ingredient not found")),
                        Map.Entry::getValue
                ));
        stock.setIngredientStockMap(ingredientStock);

        Stock updatedStock = stockRepository.save(stock);
        return mapToDto(updatedStock);
    }

    @Override
    public String deleteStock(Long id) {
        Stock stock = stockRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Stock not found"));
        stockRepository.delete(stock);
        return "stock deleted successfully";
    }

    private StockDtoResponse mapToDto(Stock stock) {
        StockDtoResponse stockDtoResponse = new StockDtoResponse();
        stockDtoResponse.setId(stock.getId());

        Map<Long, Double> ingredientStockDto = stock.getIngredientStockMap().entrySet().stream()
                .collect(Collectors.toMap(
                        entry -> entry.getKey().getId(),  // Get the ingredient ID
                        Map.Entry::getValue  // Get the quantity
                ));
        stockDtoResponse.setIngredientStockMap(ingredientStockDto);

        return stockDtoResponse;
    }
    private Stock mapToEntity(StockDtoRequest stockDtoRequest) {
        Stock stock = new Stock();

        Map<Ingredient, Double> ingredientStock = stockDtoRequest.getIngredientStockMap().entrySet().stream()
                .collect(Collectors.toMap(
                        entry -> ingredientRepository.findById(entry.getKey())
                                .orElseThrow(() -> new RuntimeException("Ingredient not found")),
                        Map.Entry::getValue
                ));
        stock.setIngredientStockMap(ingredientStock);

        return stock;
    }


}