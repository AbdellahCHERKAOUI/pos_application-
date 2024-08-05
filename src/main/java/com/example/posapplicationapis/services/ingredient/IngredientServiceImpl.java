package com.example.posapplicationapis.services.ingredient;

import com.example.posapplicationapis.dto.ingridient.IngredientDtoRequest;
import com.example.posapplicationapis.entities.Ingredient;
import com.example.posapplicationapis.repositories.IngredientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class IngredientServiceImpl implements IngredientService {
    @Autowired
    private IngredientRepository ingredientRepository;

    public Ingredient addIngredient(IngredientDtoRequest ingredientDtoRequest) {
        Ingredient ingredient = new Ingredient();
        ingredient.setName(ingredientDtoRequest.getName());
        ingredient.setUnitType(ingredientDtoRequest.getUnitType());



        return ingredientRepository.save(ingredient);
    }

    public Ingredient updateIngredient(Long id, IngredientDtoRequest ingredientDtoRequest) {
        Ingredient existingIngredient = ingredientRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ingredient not found"));

        existingIngredient.setName(ingredientDtoRequest.getName());
        existingIngredient.setUnitType(ingredientDtoRequest.getUnitType());

        return ingredientRepository.save(existingIngredient);
    }

    public void removeIngredient(Long id) {
        Ingredient existingIngredient = ingredientRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ingredient not found"));

        ingredientRepository.delete(existingIngredient);
    }
}
