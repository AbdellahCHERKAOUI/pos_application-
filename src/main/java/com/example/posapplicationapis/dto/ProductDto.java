package com.example.posapplicationapis.dto;


import java.util.List;

public class ProductDto {


    private Long id;

    private String name;

    private Double salesPrice;
    private Double vipPrice;
    private Double tax;
    private Double price;



    private CategoryDto category;
    private List<ProductIngredientDto> ingredients;

    private List<SupplementDto> supplements;


}

