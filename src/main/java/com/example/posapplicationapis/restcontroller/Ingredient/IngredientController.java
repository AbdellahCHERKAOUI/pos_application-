package com.example.posapplicationapis.restcontroller.Ingredient;

import com.example.posapplicationapis.dto.ingridient.IngredientDtoRequest;
import com.example.posapplicationapis.entities.Ingredient;
import com.example.posapplicationapis.services.ingredient.IngredientService;
import com.example.posapplicationapis.services.ingredient.IngredientServiceImpl;
import com.example.posapplicationapis.repositories.IngredientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/ingredient")
public class IngredientController {

    @Autowired
    private IngredientService ingredientService;
    @Autowired
    private IngredientServiceImpl ingredientServiceImpl;

    @PostMapping("/add-ingredient")
    public Ingredient addIngredient(@RequestBody IngredientDtoRequest ingredientDtoRequest) {
        return ingredientServiceImpl.addIngredient(ingredientDtoRequest);
    }

    @PutMapping("/{id}/update-ingredient")
    public Ingredient updateIngredient(@PathVariable Long id, @RequestBody IngredientDtoRequest ingredientDtoRequest) {
        return ingredientServiceImpl.updateIngredient(id, ingredientDtoRequest);
    }

    @DeleteMapping("/{id}/remove-ingredient")
    public void removeIngredient(@PathVariable Long id) {
        ingredientServiceImpl.removeIngredient(id);
    }
}