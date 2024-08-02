package com.example.posapplicationapis.dto.productIngredient;

import com.example.posapplicationapis.dto.ingridient.IngredientDtoResponse;
import lombok.Data;

import java.util.List;
@Data
public class ProductIngredientDtoResponse {
    private Long id;

    private double quantity;


    private List<IngredientDtoResponse> ingredients;
}
