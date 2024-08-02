package com.example.posapplicationapis.dto.product;

import com.example.posapplicationapis.dto.category.CategoryDtoResponse;
import com.example.posapplicationapis.dto.productIngredient.ProductIngredientDtoResponse;
import com.example.posapplicationapis.dto.supplement.SupplementDtoResponse;
import lombok.Data;

import java.util.List;

@Data
public class ProductDtoResponse {
    private Long id;
    private String name;
    private Double salesPrice;
    private Double vipPrice;
    private Double tax;
    private Double price;
    private CategoryDtoResponse category;
    private List<ProductIngredientDtoResponse> ingredients;
    private List<SupplementDtoResponse> supplements;
}
