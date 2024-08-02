package com.example.posapplicationapis.restcontroller.productIngredient;

import com.example.posapplicationapis.dto.productIngredient.ProductIngredientDtoRequest;
import com.example.posapplicationapis.dto.productIngredient.ProductIngredientDtoResponse;
import com.example.posapplicationapis.services.productIngredient.ProductIngredientService;
import com.example.posapplicationapis.services.productIngredient.ProductIngredientServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/product-ingredient")
public class ProductIngredientController {
    private ProductIngredientService productIngredientService;

    public ProductIngredientController(ProductIngredientServiceImpl productIngredientService) {
        this.productIngredientService = productIngredientService;
    }
    @PostMapping("/add-product-ingredient")
    public ResponseEntity<ProductIngredientDtoResponse> createProductIngredient(@RequestBody ProductIngredientDtoRequest requestDto) {
        ProductIngredientDtoResponse responseDto = productIngredientService.createProductIngredient(requestDto);
        return ResponseEntity.ok(responseDto);
    }
    @PutMapping("/update-product-ingredient/{id}")
    public ResponseEntity<ProductIngredientDtoResponse> updateProductIngredient(@PathVariable Long id, @RequestBody ProductIngredientDtoRequest requestDto) {
        ProductIngredientDtoResponse responseDto = productIngredientService.updateProductIngredient(id, requestDto);
        return ResponseEntity.ok(responseDto);
    }
    @GetMapping("/get-product-ingredient/{id}")
    public ResponseEntity<ProductIngredientDtoResponse> getProductIngredient(@PathVariable Long id) {
        ProductIngredientDtoResponse responseDto = productIngredientService.getProductIngredient(id);
        return ResponseEntity.ok(responseDto);
    }
    @GetMapping(value = "/get-all-product-ingredient")
    public ResponseEntity<List<ProductIngredientDtoResponse>> getAllProductIngredients() {
        List<ProductIngredientDtoResponse> responseDtos = productIngredientService.getAllProductIngredients();
        return ResponseEntity.ok(responseDtos);
    }
    @DeleteMapping("/delete-product-ingredient/{id}")
    public ResponseEntity<String> deleteProductIngredient(@PathVariable Long id) {
        String message = productIngredientService.deleteProductIngredient(id);
        return ResponseEntity.ok(message);
    }
}
