package com.example.posapplicationapis.dto.product;

import com.example.posapplicationapis.entities.Ingredient;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class ProductDtoResponse {
    private Long id;
    private String name;
    private Double salesPrice;
    private Double vipPrice;
    private Double tax;
    private Double price;
    private String categoryName;
    private List<String> supplementNames;
    private Map<String, Double> productIngredients;
    private String image;
}
