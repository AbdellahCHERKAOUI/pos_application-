package com.example.posapplicationapis.dto.productIngredient;
import lombok.Data;

import java.util.Arrays;
import java.util.List;


@Data
public class ProductIngredientDtoRequest {

   private double quantity;

   private List<Long> ingredientIds;
}

