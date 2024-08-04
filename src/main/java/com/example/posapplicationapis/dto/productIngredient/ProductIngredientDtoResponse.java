package com.example.posapplicationapis.dto.productIngredient;

import com.example.posapplicationapis.dto.ingridient.IngredientDtoResponse;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class ProductIngredientDtoResponse {
    private Long id;
    private Map<Long, Double> ingredientQuantities;
}
