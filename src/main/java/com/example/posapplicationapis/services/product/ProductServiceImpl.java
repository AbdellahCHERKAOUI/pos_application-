package com.example.posapplicationapis.services.product;

import com.example.posapplicationapis.dto.product.ProductDtoRequest;
import com.example.posapplicationapis.dto.product.ProductDtoResponse;

import com.example.posapplicationapis.entities.Category;
import com.example.posapplicationapis.entities.Product;
import com.example.posapplicationapis.entities.ProductIngredient;
import com.example.posapplicationapis.entities.Supplement;
import com.example.posapplicationapis.repositories.CategoryRepository;
import com.example.posapplicationapis.repositories.ProductIngredientRepository;
import com.example.posapplicationapis.repositories.ProductRepository;
import com.example.posapplicationapis.repositories.SupplementRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ProductIngredientRepository productIngredientRepository;
    private final SupplementRepository supplementRepository;

    @Autowired
    private ModelMapper modelMapper;

    public ProductServiceImpl(ProductRepository productRepository, CategoryRepository categoryRepository, ProductIngredientRepository productIngredientRepository, SupplementRepository supplementRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.productIngredientRepository = productIngredientRepository;
        this.supplementRepository = supplementRepository;
    }
    @Override
    public ProductDtoResponse createProduct(ProductDtoRequest requestDto) {
        Product product = modelMapper.map(requestDto, Product.class);

        // Set the category
        product.setCategory(categoryRepository.findById(requestDto.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found")));

        // Fetch the supplements by their names
        List<Supplement> supplements = requestDto.getSupplementNames().stream()
                .map(name -> supplementRepository.findByName(name)
                        .orElseThrow(() -> new RuntimeException("Supplement not found: " + name)))
                .collect(Collectors.toList());
        product.setSupplements(supplements);

        List<ProductIngredient> productIngredients = requestDto.getIngredientIds().stream()
                .map(id -> productIngredientRepository.findById(id)
                        .orElseThrow(() -> new RuntimeException("ProductIngredient not found: " + id)))
                .collect(Collectors.toList());
        product.setIngredients(productIngredients);

        // Save the product
        Product savedProduct = productRepository.save(product);
        return modelMapper.map(savedProduct, ProductDtoResponse.class);
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
