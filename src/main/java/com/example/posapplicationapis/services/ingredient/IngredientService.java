package com.example.posapplicationapis.services.ingredient;

import com.example.posapplicationapis.dto.category.CategoryDtoRequest;
import com.example.posapplicationapis.dto.ingridient.IngredientDtoRequest;
import com.example.posapplicationapis.entities.Category;
import com.example.posapplicationapis.entities.Ingredient;

import java.util.List;

interface IngredientService {

    Ingredient addIngredient(IngredientDtoRequest ingredientDtoRequest);

    Ingredient updateIngredient(Long id, IngredientDtoRequest ingredientDtoRequest);

    void removeIngredient(Long id);


}
