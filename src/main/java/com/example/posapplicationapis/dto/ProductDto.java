package com.example.posapplicationapis.dto;


import com.example.posapplicationapis.dto.category.CategoryDtoResponse;
import com.example.posapplicationapis.dto.productIngredient.ProductIngredientDtoRequest;
import com.example.posapplicationapis.dto.supplement.SupplementDtoRequest;

import java.util.List;

public class ProductDto {


    private Long id;

    private String name;

    private Double salesPrice;
    private Double vipPrice;
    private Double tax;
    private Double price;



    private CategoryDtoResponse category;
    private List<ProductIngredientDtoRequest> ingredients;

    private List<SupplementDtoRequest> supplements;


}

