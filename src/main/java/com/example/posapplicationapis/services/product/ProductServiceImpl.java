package com.example.posapplicationapis.services.product;

import com.example.posapplicationapis.dto.product.ProductDtoRequest;
import com.example.posapplicationapis.dto.product.ProductDtoResponse;

import com.example.posapplicationapis.dto.productIngredient.ProductIngredientDtoResponse;
import com.example.posapplicationapis.dto.supplement.SupplementDtoResponse;
import com.example.posapplicationapis.entities.*;
import com.example.posapplicationapis.repositories.*;
import com.example.posapplicationapis.service.ImageService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {
    private final IngredientRepository ingredientRepository;
    private  ProductRepository productRepository;
    private  CategoryRepository categoryRepository;
    private  ProductIngredientRepository productIngredientRepository;
    private  SupplementRepository supplementRepository;
    private ImageService imageService;
    private ModelMapper modelMapper;
    private ImageRepository imageRepository;

    public ProductServiceImpl(IngredientRepository ingredientRepository, ProductRepository productRepository, CategoryRepository categoryRepository, ProductIngredientRepository productIngredientRepository, SupplementRepository supplementRepository, ImageService imageService, ModelMapper modelMapper, ImageRepository imageRepository) {
        this.ingredientRepository = ingredientRepository;
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.productIngredientRepository = productIngredientRepository;
        this.supplementRepository = supplementRepository;
        this.imageService = imageService;
        this.modelMapper = modelMapper;
        this.imageRepository = imageRepository;
    }

    @Override
    @Transactional
    public ProductDtoResponse createProduct(ProductDtoRequest requestDto) {
        Product product = mapToProduct(requestDto);
        product.setId(null);

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

        // Fetch ingredients
        Map<Ingredient, Double> ingredientsAndQuantities = new HashMap<>();

        for (Map.Entry<Long, Double> entry : requestDto.getIngredientAndQuantities().entrySet()) {
            Long ingredientId = entry.getKey();
            Double quantity = entry.getValue();

            // Fetch the ingredient by ID using the repository
            Optional<Ingredient> optionalIngredient = ingredientRepository.findById(ingredientId);

            if (optionalIngredient.isPresent()) {
                Ingredient ingredient = optionalIngredient.get();
                ingredientsAndQuantities.put(ingredient, quantity);
            } else {
                // Handle the case where the ingredient with the given ID doesn't exist
                throw new EntityNotFoundException("Ingredient with ID " + ingredientId + " not found");
            }
        }

        ProductIngredient productIngredient = new ProductIngredient();
        productIngredient.setIngredientQuantities(ingredientsAndQuantities);
        product.setIngredients(new ArrayList<>(List.of(productIngredient)));

        productIngredientRepository.save(productIngredient);

        // Save the product
        Product savedProduct = productRepository.save(product);

        return mapToDto(savedProduct);
    }


    @Override
    public List<ProductDtoResponse> getAllProducts() {
        return productRepository.findAll().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public ProductDtoResponse getProduct(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        return mapToDto(product);
    }

  @Override
  public ProductDtoResponse updateProduct(Long productId, ProductDtoRequest requestDto) {
      // Fetch the existing product
      Product product = productRepository.findById(productId)
              .orElseThrow(() -> new RuntimeException("Product not found"));

      // Manually map non-ID fields from the request DTO to the entity
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
      // Fetch ingredients
      Map<Ingredient, Double> ingredientsAndQuantities = new HashMap<>();

      for (Map.Entry<Long, Double> entry : requestDto.getIngredientAndQuantities().entrySet()) {
          Long ingredientId = entry.getKey();
          Double quantity = entry.getValue();

          // Fetch the ingredient by ID using the repository
          Optional<Ingredient> optionalIngredient = ingredientRepository.findById(ingredientId);

          if (optionalIngredient.isPresent()) {
              Ingredient ingredient = optionalIngredient.get();
              ingredientsAndQuantities.put(ingredient, quantity);
          } else {
              // Handle the case where the ingredient with the given ID doesn't exist
              throw new EntityNotFoundException("Ingredient with ID " + ingredientId + " not found");
          }
      }

      ProductIngredient productIngredient = new ProductIngredient();
      productIngredient.setIngredientQuantities(ingredientsAndQuantities);
      product.setIngredients(List.of(productIngredient));

      productIngredientRepository.save(productIngredient);
      product.setIngredients(new ArrayList<>(List.of(productIngredient)));

      // Fetch supplements by names and set them
      List<Supplement> supplements = requestDto.getSupplementNames().stream()
              .map(name -> supplementRepository.findByName(name)
                      .orElseThrow(() -> new RuntimeException("Supplement not found: " + name)))
              .collect(Collectors.toList());
      product.setSupplements(supplements);

      // Save the updated product
      Product updatedProduct = productRepository.save(product);

      // Map the updated product to response DTO
      return mapToDto(updatedProduct);
  }





    @Override
    public String deleteProduct(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        productRepository.delete(product);
        return "Product deleted successfully";
    }

    @Override
    public ProductDtoResponse addImage(Long id, MultipartFile image) throws IOException {
        // Fetch the product by ID
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        // Upload the new image and get the link
        String link = imageService.uploadFile(image);
        if (link == null) {
            throw new RuntimeException("Failed to upload the image, link is null");
        }

        // Create a new Image object and save it
        Image newImage = new Image();
        newImage.setLink(link);
        imageRepository.save(newImage);

        // Remove the old image if it exists
        Image oldImage = product.getImage();
        if (oldImage != null) {
            product.setImage(null);
            imageRepository.delete(oldImage); // Ensure oldImage is properly handled and removed
        }

        // Set the new image to the product and save the product
        product.setImage(newImage);
        Product updatedProduct = productRepository.save(product);

        // Return the updated product DTO
        return mapToDto(updatedProduct);
    }

    // Mapping from Product entity to ProductDtoResponse
    private ProductDtoResponse mapToDto(Product product) {
        ProductDtoResponse responseDto = new ProductDtoResponse();

        // Map simple fields
        responseDto.setId(product.getId());
        responseDto.setName(product.getName());
        responseDto.setSalesPrice(product.getSalesPrice());
        responseDto.setVipPrice(product.getVipPrice());
        responseDto.setTax(product.getTax());
        responseDto.setPrice(product.getPrice());

        // Map category (only the ID)
        if (product.getCategory() != null) {
            responseDto.setCategoryName(product.getCategory().getName());
        }
        if (product.getImage() != null){
            responseDto.setImage(product.getImage().getLink());
        }

        // Map supplements (names)
        if (product.getSupplements() != null && !product.getSupplements().isEmpty()) {
            List<String> supplementNames = product.getSupplements().stream()
                    .map(Supplement::getName)
                    .collect(Collectors.toList());
            responseDto.setSupplementNames(supplementNames);
        }

        // Map product ingredients (IDs)
        if (product.getIngredients() != null && !product.getIngredients().isEmpty()) {
            Map<String, Double> productIngredientNames = product.getIngredients().stream()
                    .map(ProductIngredient::getIngredientQuantities)
                    .flatMap(map -> map.entrySet().stream())
                    .collect(Collectors.toMap(
                            entry -> entry.getKey().getName(),
                            Map.Entry::getValue,
                            (existingValue, newValue) -> existingValue));
            responseDto.setProductIngredients(productIngredientNames);
        }

        return responseDto;
    }
    public Product mapToProduct(ProductDtoRequest requestDto) {

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

        // Fetch and set the supplements by their names
        List<Supplement> supplements = requestDto.getSupplementNames().stream()
                .map(name -> supplementRepository.findByName(name)
                        .orElseThrow(() -> new RuntimeException("Supplement not found: " + name)))
                .collect(Collectors.toList());
        product.setSupplements(supplements);

        // Fetch and set ingredients with quantities
        Map<Ingredient, Double> ingredientsAndQuantities = requestDto.getIngredientAndQuantities().entrySet().stream()
                .collect(Collectors.toMap(
                        entry -> ingredientRepository.findById(entry.getKey())
                                .orElseThrow(() -> new RuntimeException("Ingredient not found with ID: " + entry.getKey())),
                        Map.Entry::getValue
                ));

        ProductIngredient productIngredient = new ProductIngredient();
        productIngredient.setIngredientQuantities(ingredientsAndQuantities);
        product.setIngredients(List.of(productIngredient));

        return product;
    }

}
