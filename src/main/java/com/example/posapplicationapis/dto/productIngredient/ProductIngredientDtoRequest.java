package com.example.posapplicationapis.dto.productIngredient;
import lombok.Data;


import java.util.Map;


@Data
public class ProductIngredientDtoRequest {
   private Map<Long, Double> ingredientQuantities;
}

