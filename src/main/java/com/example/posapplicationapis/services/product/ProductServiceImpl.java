package com.example.posapplicationapis.services.product;

import com.example.posapplicationapis.dto.product.ProductDtoRequest;
import com.example.posapplicationapis.dto.product.ProductDtoResponse;

import com.example.posapplicationapis.dto.productIngredient.ProductIngredientDtoResponse;
import com.example.posapplicationapis.dto.supplement.SupplementDtoResponse;
import com.example.posapplicationapis.entities.*;
import com.example.posapplicationapis.repositories.CategoryRepository;
import com.example.posapplicationapis.repositories.ProductIngredientRepository;
import com.example.posapplicationapis.repositories.ProductRepository;
import com.example.posapplicationapis.repositories.SupplementRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {
    private  ProductRepository productRepository;
    private  CategoryRepository categoryRepository;
    private  ProductIngredientRepository productIngredientRepository;
    private  SupplementRepository supplementRepository;

    private ModelMapper modelMapper;

    public ProductServiceImpl(ProductRepository productRepository, CategoryRepository categoryRepository,
                              ProductIngredientRepository productIngredientRepository, SupplementRepository supplementRepository
                               ,ModelMapper modelMapper
    ) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.productIngredientRepository = productIngredientRepository;
        this.supplementRepository = supplementRepository;
        this.modelMapper=modelMapper;

    }

   /* @Override
    public ProductDtoResponse createProduct(ProductDtoRequest requestDto) {
        Product product = modelMapper.map(requestDto, Product.class);

        product.setCategory(categoryRepository.findById(requestDto.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found")));

        List<Supplement> supplements = requestDto.getSupplementNames().stream()
                .map(name -> supplementRepository.findByName(name)
                        .orElseThrow(() -> new RuntimeException("Supplement not found: " + name)))
                .collect(Collectors.toList());
        product.setSupplements(supplements);

        // Manually map ingredient quantities
        List<ProductIngredient> productIngredients = requestDto.getIngredientIds().stream()
                .map(id -> productIngredientRepository.findById(id)
                        .orElseThrow(() -> new RuntimeException("ProductIngredient not found: " + id)))
                .collect(Collectors.toList());
        product.setIngredients(productIngredients);

        Product savedProduct = productRepository.save(product);
        return modelMapper.map(savedProduct, ProductDtoResponse.class);
    }*/

    @Override
    public ProductDtoResponse createProduct(ProductDtoRequest requestDto) {
        // Manually map ProductDtoRequest to Product entity
        Product product = new Product();
        product.setName(requestDto.getName());
        product.setSalesPrice(requestDto.getSalesPrice());
        product.setVipPrice(requestDto.getVipPrice());
        product.setTax(requestDto.getTax());
        product.setPrice(requestDto.getPrice());



        // Set the category
        Category category = categoryRepository.findById(requestDto.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found"));
        product.setCategory(category);

        // Fetch the supplements by their names
        List<Supplement> supplements = requestDto.getSupplementNames().stream()
                .map(name -> supplementRepository.findByName(name)
                        .orElseThrow(() -> new RuntimeException("Supplement not found: " + name)))
                .collect(Collectors.toList());
        product.setSupplements(supplements);

        // Fetch the product ingredients by their IDs
        List<ProductIngredient> productIngredients = requestDto.getIngredientIds().stream()
                .map(id -> productIngredientRepository.findById(id)
                        .orElseThrow(() -> new RuntimeException("ProductIngredient not found: " + id)))
                .collect(Collectors.toList());
        product.setIngredients(productIngredients);

        // Save the product
        Product savedProduct = productRepository.save(product);

        // Manually map Product entity to ProductDtoResponse
        ProductDtoResponse responseDto = new ProductDtoResponse();
        responseDto.setId(savedProduct.getId());
        responseDto.setName(savedProduct.getName());
        responseDto.setSalesPrice(savedProduct.getSalesPrice());
        responseDto.setVipPrice(savedProduct.getVipPrice());
        responseDto.setTax(savedProduct.getTax());
        responseDto.setPrice(savedProduct.getPrice());
        responseDto.setCategoryId(savedProduct.getCategory().getId());
        responseDto.setSupplementNames(savedProduct.getSupplements().stream()
                .map(Supplement::getName)
                .collect(Collectors.toList()));
        responseDto.setIngredientIds(savedProduct.getIngredients().stream()
                .map(ProductIngredient::getId)
                .collect(Collectors.toList()));

        return responseDto;
    }



    @Override
    public List<ProductDtoResponse> getAllProducts() {
        return productRepository.findAll().stream()
                .map(product -> modelMapper.map(product, ProductDtoResponse.class))
                .collect(Collectors.toList());
    }

    @Override
    public ProductDtoResponse getProduct(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        return modelMapper.map(product, ProductDtoResponse.class);
    }

    @Override
    public ProductDtoResponse updateProduct(Long productId, ProductDtoRequest requestDto) {
        // Fetch the existing product
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        // Update product details from the request DTO
        product.setName(requestDto.getName());
        product.setSalesPrice(requestDto.getSalesPrice());
        product.setVipPrice(requestDto.getVipPrice());
        product.setTax(requestDto.getTax());
        product.setPrice(requestDto.getPrice());

        // Fetch the category and set it
        Category category = categoryRepository.findById(requestDto.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found"));
        product.setCategory(category);

        // Fetch ingredients by IDs and set them
        List<ProductIngredient> ingredients = requestDto.getIngredientIds().stream()
                .map(id -> productIngredientRepository.findById(id)
                        .orElseThrow(() -> new RuntimeException("Ingredient not found: " + id)))
                .collect(Collectors.toList());
        product.setIngredients(ingredients);

        // Fetch supplements by names and set them
        List<Supplement> supplements = requestDto.getSupplementNames().stream()
                .map(name -> supplementRepository.findByName(name)
                        .orElseThrow(() -> new RuntimeException("Supplement not found: " + name)))
                .collect(Collectors.toList());
        product.setSupplements(supplements);

        // Save the updated product
        Product updatedProduct = productRepository.save(product);

        // Map the updated product to response DTO
        return modelMapper.map(updatedProduct, ProductDtoResponse.class);
    }

    /*@Override
    public ProductDtoResponse updateProduct(Long id, ProductDtoRequest requestDto) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        modelMapper.map(requestDto, product);
        product.setCategory(categoryRepository.findById(requestDto.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found")));
        Product updatedProduct = productRepository.save(product);
        return modelMapper.map(updatedProduct, ProductDtoResponse.class);
    }*/


    @Override
    public String deleteProduct(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        productRepository.delete(product);
        return "Product deleted successfully";
    }
}
