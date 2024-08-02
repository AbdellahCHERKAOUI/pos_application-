package com.example.posapplicationapis.services.productIngredient;

import com.example.posapplicationapis.dto.productIngredient.ProductIngredientDtoRequest;
import com.example.posapplicationapis.dto.productIngredient.ProductIngredientDtoResponse;
import com.example.posapplicationapis.entities.ProductIngredient;
import com.example.posapplicationapis.repositories.IngredientRepository;
import com.example.posapplicationapis.repositories.ProductIngredientRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
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
        ProductIngredient productIngredient = modelMapper.map(requestDto, ProductIngredient.class);
        productIngredient.setIngredients(
                requestDto.getIngredientIds().stream()
                        .map(id -> ingredientRepository.findById(id).orElseThrow(() -> new RuntimeException("Ingredient not found")))
                        .collect(Collectors.toList())
        );
        ProductIngredient savedProductIngredient = productIngredientRepository.save(productIngredient);
        return modelMapper.map(savedProductIngredient, ProductIngredientDtoResponse.class);

    }

    @Override
    public List<ProductIngredientDtoResponse> getAllProductIngredients() {
        return productIngredientRepository.findAll().stream()
                .map(productIngredient -> modelMapper.map(productIngredient, ProductIngredientDtoResponse.class))
                .collect(Collectors.toList());
    }

    @Override
    public ProductIngredientDtoResponse getProductIngredient(Long id) {
        ProductIngredient productIngredient = productIngredientRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("ProductIngredient not found"));
        return modelMapper.map(productIngredient, ProductIngredientDtoResponse.class);
    }

    @Override
    public ProductIngredientDtoResponse updateProductIngredient(Long id, ProductIngredientDtoRequest requestDto) {
        ProductIngredient productIngredient = productIngredientRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("ProductIngredient not found"));

        modelMapper.map(requestDto, productIngredient);
        productIngredient.setIngredients(
                requestDto.getIngredientIds().stream()
                        .map(ingredientId -> ingredientRepository.findById(ingredientId).orElseThrow(() -> new RuntimeException("Ingredient not found")))
                        .collect(Collectors.toList())
        );

        ProductIngredient updatedProductIngredient = productIngredientRepository.save(productIngredient);
        return modelMapper.map(updatedProductIngredient, ProductIngredientDtoResponse.class);

    }

    @Override
    public String deleteProductIngredient(Long id) {
        ProductIngredient productIngredient = productIngredientRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("ProductIngredient not found"));
        productIngredientRepository.delete(productIngredient);
        return "ProductIngredient deleted successfully";
    }




}
