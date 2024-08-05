package com.example.posapplicationapis.dto.productIngredient;
import lombok.Data;

import java.util.Arrays;
import java.util.List;
import java.util.Map;


@Data
public class ProductIngredientDtoRequest {
   private Map<Long, Double> ingredientQuantities;
}

