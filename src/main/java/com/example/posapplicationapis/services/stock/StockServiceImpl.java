package com.example.posapplicationapis.services.stock;

import com.example.posapplicationapis.dto.stock.StockDtoRequest;
import com.example.posapplicationapis.dto.stock.StockDtoResponse;
import com.example.posapplicationapis.entities.Ingredient;
import com.example.posapplicationapis.entities.Stock;
import com.example.posapplicationapis.repositories.IngredientRepository;
import com.example.posapplicationapis.repositories.StockRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

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
        Stock stock = mapToEntity(stockDtoRequest);
        Stock savedStock = stockRepository.save(stock);
        return mapToDto(savedStock);
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