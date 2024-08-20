package com.example.posapplicationapis.services.ingredient;

import com.example.posapplicationapis.dto.ingridient.IngredientDtoRequest;
import com.example.posapplicationapis.dto.ingridient.IngredientDtoResponse;
import com.example.posapplicationapis.dto.stock.StockDtoRequest;
import com.example.posapplicationapis.entities.Ingredient;
import com.example.posapplicationapis.entities.Stock;
import com.example.posapplicationapis.repositories.IngredientRepository;
import com.example.posapplicationapis.repositories.StockRepository;
import com.example.posapplicationapis.services.stock.StockServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class IngredientServiceImpl implements IngredientService {
    @Autowired
    private IngredientRepository ingredientRepository;
    @Autowired
    private StockServiceImpl stockServiceImpl;
    @Autowired
    private StockRepository stockRepository;

    public IngredientDtoResponse addIngredient(IngredientDtoRequest ingredientDtoRequest) {
        // Find the ingredient by name
        Optional<Ingredient> optionalIngredient = ingredientRepository.findByName(ingredientDtoRequest.getName());
        Ingredient savedIngredient;

        if (optionalIngredient.isEmpty()) {
            // If the ingredient is not found, create a new one
            Ingredient ingredient = new Ingredient();
            ingredient.setName(ingredientDtoRequest.getName());
            ingredient.setUnitType(ingredientDtoRequest.getUnitType());
            savedIngredient = ingredientRepository.save(ingredient);
        } else {
            // If the ingredient is found, update its details
            Ingredient ingredientInDB = optionalIngredient.get();
            ingredientInDB.setName(ingredientDtoRequest.getName());
            ingredientInDB.setUnitType(ingredientDtoRequest.getUnitType());
            savedIngredient = ingredientRepository.save(ingredientInDB);
        }

        // Making the Map
        Map<Long, Double> ingredientQuantity = new HashMap<>();
        ingredientQuantity.put(savedIngredient.getId(), ingredientDtoRequest.getQuantity());

        // Creating StockRequest
        StockDtoRequest stockDtoRequest = new StockDtoRequest();
        stockDtoRequest.setIngredientStockMap(ingredientQuantity);
        stockServiceImpl.createStock(stockDtoRequest);

        return toDto(savedIngredient);
    }

    public IngredientDtoResponse updateIngredient(Long id, IngredientDtoRequest ingredientDtoRequest) {
        Ingredient existingIngredient = ingredientRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ingredient not found"));

        existingIngredient.setName(ingredientDtoRequest.getName());
        existingIngredient.setUnitType(ingredientDtoRequest.getUnitType());

        return toDto(ingredientRepository.save(existingIngredient));
    }

    public void removeIngredient(Long id) {
        Ingredient existingIngredient = ingredientRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ingredient not found"));
        Stock stock = stockRepository.findAll().get(0);
        Map<Ingredient, Double> stockMap = stock.getIngredientStockMap();
        stockMap.remove(existingIngredient);
        stock.setIngredientStockMap(stockMap);
        stockRepository.save(stock);

        ingredientRepository.delete(existingIngredient);
    }

    @Override
    public List<IngredientDtoResponse> getAll() {
        List<IngredientDtoResponse> ingredientDtoResponses = new ArrayList<>();

        for(Ingredient ingredient : ingredientRepository.findAll()){
            ingredientDtoResponses.add(toDto(ingredient));
        }


        return ingredientDtoResponses;
    }

    private IngredientDtoResponse toDto(Ingredient ingredient) {
        IngredientDtoResponse ingredientDtoResponse =  new IngredientDtoResponse();
        ingredientDtoResponse.setId(ingredient.getId());
        ingredientDtoResponse.setName(ingredient.getName());
        ingredientDtoResponse.setUnitType(ingredient.getUnitType());
        ingredientDtoResponse.setQuantity(stockRepository.findAll().get(0).getIngredientStockMap().get(ingredient));

        return ingredientDtoResponse;

    }
}
