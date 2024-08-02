package com.example.posapplicationapis.dto.product;


import com.example.posapplicationapis.dto.category.CategoryDtoResponse;
import com.example.posapplicationapis.dto.productIngredient.ProductIngredientDtoRequest;
import com.example.posapplicationapis.dto.supplement.SupplementDtoRequest;
import lombok.Data;

import java.util.List;
@Data
public class ProductDtoRequest {


    private String name;
    private Double salesPrice;
    private Double vipPrice;
    private Double tax;
    private Double price;
    private Long categoryId;
    private List<ProductIngredientDtoRequest> ingredients;
    private List<SupplementDtoRequest> supplements;


}

