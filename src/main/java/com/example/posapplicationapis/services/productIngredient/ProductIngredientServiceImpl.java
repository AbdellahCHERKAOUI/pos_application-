package com.example.posapplicationapis.services.productIngredient;

import com.example.posapplicationapis.dto.productIngredient.ProductIngredientDtoRequest;
import com.example.posapplicationapis.dto.productIngredient.ProductIngredientDtoResponse;
import com.example.posapplicationapis.entities.ProductIngredient;
import com.example.posapplicationapis.entities.Ingredient;
import com.example.posapplicationapis.repositories.IngredientRepository;
import com.example.posapplicationapis.repositories.ProductIngredientRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ProductIngredientServiceImpl implements ProductIngredientService {
    private final ProductIngredientRepository productIngredientRepository;
    private final IngredientRepository ingredientRepository;

    @Autowired
    private ModelMapper modelMapper;

    public ProductIngredientServiceImpl(ProductIngredientRepository productIngredientRepository, IngredientRepository ingredientRepository) {
        this.productIngredientRepository = productIngredientRepository;
        this.ingredientRepository = ingredientRepository;
    }

    @Override
    public ProductIngredientDtoResponse createProductIngredient(ProductIngredientDtoRequest requestDto) {
        ProductIngredient productIngredient = new ProductIngredient();

        // Map the ingredientQuantities from the DTO to the entity
        Map<Ingredient, Double> ingredientQuantities = requestDto.getIngredientQuantities().entrySet().stream()
                .collect(Collectors.toMap(
                        entry -> ingredientRepository.findById(entry.getKey())
                                .orElseThrow(() -> new RuntimeException("Ingredient not found")),
                        Map.Entry::getValue
                ));

        productIngredient.setIngredientQuantities(ingredientQuantities);

        // Save the entity and map the result to a response DTO
        ProductIngredient savedProductIngredient = productIngredientRepository.save(productIngredient);
        ProductIngredientDtoResponse responseDto = new ProductIngredientDtoResponse();
        responseDto.setId(savedProductIngredient.getId());

        // Map the saved entity's ingredientQuantities to the response DTO
        Map<Long, Double> ingredientQuantitiesResponse = savedProductIngredient.getIngredientQuantities().entrySet().stream()
                .collect(Collectors.toMap(
                        entry -> entry.getKey().getId(),
                        Map.Entry::getValue
                ));

        responseDto.setIngredientQuantities(ingredientQuantitiesResponse);

        return responseDto;
    }

    @Override
    public List<ProductIngredientDtoResponse> getAllProductIngredients() {
        return productIngredientRepository.findAll().stream()
                .map(productIngredient -> {
                    ProductIngredientDtoResponse responseDto = new ProductIngredientDtoResponse();
                    responseDto.setId(productIngredient.getId());
                    responseDto.setIngredientQuantities(productIngredient.getIngredientQuantities().entrySet().stream()
                            .collect(Collectors.toMap(
                                    entry -> entry.getKey().getId(),
                                    Map.Entry::getValue
                            )));
                    return responseDto;
                })
                .collect(Collectors.toList());
    }

    @Override
    public ProductIngredientDtoResponse getProductIngredient(Long id) {
        ProductIngredient productIngredient = productIngredientRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("ProductIngredient not found"));

        ProductIngredientDtoResponse responseDto = new ProductIngredientDtoResponse();
        responseDto.setId(productIngredient.getId());
        responseDto.setIngredientQuantities(productIngredient.getIngredientQuantities().entrySet().stream()
                .collect(Collectors.toMap(
                        entry -> entry.getKey().getId(),
                        Map.Entry::getValue
                )));

        return responseDto;
    }

    @Override
    public ProductIngredientDtoResponse updateProductIngredient(Long id, ProductIngredientDtoRequest requestDto) {
        ProductIngredient productIngredient = productIngredientRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("ProductIngredient not found"));

        Map<Ingredient, Double> ingredientQuantities = requestDto.getIngredientQuantities().entrySet().stream()
                .collect(Collectors.toMap(
                        entry -> ingredientRepository.findById(entry.getKey())
                                .orElseThrow(() -> new RuntimeException("Ingredient not found")),
                        Map.Entry::getValue
                ));

        productIngredient.setIngredientQuantities(ingredientQuantities);

        ProductIngredient updatedProductIngredient = productIngredientRepository.save(productIngredient);

        ProductIngredientDtoResponse responseDto = new ProductIngredientDtoResponse();
        responseDto.setId(updatedProductIngredient.getId());
        responseDto.setIngredientQuantities(updatedProductIngredient.getIngredientQuantities().entrySet().stream()
                .collect(Collectors.toMap(
                        entry -> entry.getKey().getId(),
                        Map.Entry::getValue
                )));

        return responseDto;
    }

    @Override
    public String deleteProductIngredient(Long id) {
        ProductIngredient productIngredient = productIngredientRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("ProductIngredient not found"));
        productIngredientRepository.delete(productIngredient);
        return "ProductIngredient deleted successfully";
    }
}
