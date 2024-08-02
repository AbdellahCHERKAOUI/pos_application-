package com.example.posapplicationapis.services.productIngredient;

import com.example.posapplicationapis.dto.productIngredient.ProductIngredientDtoRequest;
import com.example.posapplicationapis.dto.productIngredient.ProductIngredientDtoResponse;
import com.example.posapplicationapis.entities.ProductIngredient;

import java.util.List;

public interface ProductIngredientService {
    ProductIngredientDtoResponse createProductIngredient(ProductIngredientDtoRequest requestDto);
    List<ProductIngredientDtoResponse> getAllProductIngredients();
    ProductIngredientDtoResponse getProductIngredient(Long id);
    ProductIngredientDtoResponse updateProductIngredient(Long id, ProductIngredientDtoRequest requestDto);
    String deleteProductIngredient(Long id);
}
