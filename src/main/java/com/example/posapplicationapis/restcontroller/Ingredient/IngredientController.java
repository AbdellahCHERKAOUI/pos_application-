package com.example.posapplicationapis.restcontroller.Ingredient;

import com.example.posapplicationapis.dto.category.CategoryDtoResponse;
import com.example.posapplicationapis.dto.ingridient.IngredientDtoRequest;
import com.example.posapplicationapis.dto.ingridient.IngredientDtoResponse;
import com.example.posapplicationapis.entities.Ingredient;
import com.example.posapplicationapis.services.ingredient.IngredientServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/ingredient")
public class IngredientController {

    @Autowired
    private IngredientServiceImpl ingredientServiceImpl;

    @PostMapping("/add-ingredient")
    public IngredientDtoResponse addIngredient(@RequestBody IngredientDtoRequest ingredientDtoRequest) {
        return ingredientServiceImpl.addIngredient(ingredientDtoRequest);
    }

    @PutMapping("/{id}/update-ingredient")
    public IngredientDtoResponse updateIngredient(@PathVariable Long id, @RequestBody IngredientDtoRequest ingredientDtoRequest) {
        return ingredientServiceImpl.updateIngredient(id, ingredientDtoRequest);
    }

    @DeleteMapping("/{id}/remove-ingredient")
    public void removeIngredient(@PathVariable Long id) {
        ingredientServiceImpl.removeIngredient(id);
    }
    @GetMapping("/get-all")
    private List<IngredientDtoResponse> updateImageCategory() {
        return ingredientServiceImpl.getAll();
    }
}