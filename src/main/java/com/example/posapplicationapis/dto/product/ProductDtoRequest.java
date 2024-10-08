package com.example.posapplicationapis.dto.product;


import com.example.posapplicationapis.dto.category.CategoryDtoResponse;
import com.example.posapplicationapis.dto.productIngredient.ProductIngredientDtoRequest;
import com.example.posapplicationapis.dto.supplement.SupplementDtoRequest;
import lombok.Data;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

@Data
public class ProductDtoRequest {


    private String name;
    private Double salesPrice;
    private Double vipPrice;
    private Double tax;
    private Double price;
    private Long categoryId;
    //private List<ProductIngredientDtoRequest> ingredients;
    private Map<Long, Double> ingredientAndQuantities ;//= new ArrayList<>(); // Initialize the list
    private List<String> supplementNames;// = new ArrayList<>(); // Initialize the list
}

