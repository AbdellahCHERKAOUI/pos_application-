package com.example.posapplicationapis.services.product;

import com.example.posapplicationapis.dto.product.ProductDtoRequest;
import com.example.posapplicationapis.dto.product.ProductDtoResponse;

import com.example.posapplicationapis.dto.productIngredient.ProductIngredientDtoResponse;
import com.example.posapplicationapis.dto.supplement.SupplementDtoResponse;
import com.example.posapplicationapis.entities.*;
import com.example.posapplicationapis.repositories.*;
import com.example.posapplicationapis.service.ImageService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {
    private  ProductRepository productRepository;
    private  CategoryRepository categoryRepository;
    private  ProductIngredientRepository productIngredientRepository;
    private  SupplementRepository supplementRepository;
    private ImageService imageService;
    private ModelMapper modelMapper;
    @Autowired
    private ImageRepository imageRepository;
    public ProductServiceImpl(ProductRepository productRepository, CategoryRepository categoryRepository,
                              ProductIngredientRepository productIngredientRepository, SupplementRepository supplementRepository
                               ,ModelMapper modelMapper,ImageService imageService
    ) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.productIngredientRepository = productIngredientRepository;
        this.supplementRepository = supplementRepository;
        this.modelMapper=modelMapper;
        this.imageService=imageService;

    }

    @Override
    public ProductDtoResponse createProduct(ProductDtoRequest requestDto) {
        Product product = toEntity(requestDto);
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
    public ProductDtoResponse addImage(Long productId, MultipartFile image) throws IOException {
        Product product= productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("product not found"));

        String link = imageService.uploadFile(image);

        if (link == null) {
            throw new RuntimeException("Failed to upload the image, link is null");
        }

        Image image1 = new Image();
        image1.setLink(link);
        imageRepository.save(image1);

        product.setImage(image1);
        Product updatedProduct = productRepository.save(product);
        return mapToDto(updatedProduct);
    }

    @Override
    public List<ProductDtoResponse> getAllProductsByCategory(Long categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new RuntimeException("Category not found"));
        List<Product>products=productRepository.getAllByCategory(category);
        return products.stream()
                .map(this::mapToDto) // Assuming you have a toDto method
                .collect(Collectors.toList());
    }

    // Mapping from Product entity to ProductDtoResponse
    private ProductDtoResponse mapToDto(Product product) {
        ProductDtoResponse responseDto = modelMapper.map(product, ProductDtoResponse.class);
        responseDto.setCategoryId(product.getCategory().getId());
        responseDto.setSupplementNames(product.getSupplements().stream()
                .map(Supplement::getName)
                .collect(Collectors.toList()));
        responseDto.setIngredientIds(product.getIngredients().stream()
                .map(ProductIngredient::getId)
                .collect(Collectors.toList()));
        if (product.getImage() != null) {
            responseDto.setImageurl(product.getImage().getLink());
        } else {
            responseDto.setImageurl(null);
        }
        return responseDto;
    }
    private Product toEntity(ProductDtoRequest requestDto) {
        Product product = new Product();
        product.setName(requestDto.getName());
        product.setSalesPrice(requestDto.getSalesPrice());
        product.setVipPrice(requestDto.getVipPrice());
        product.setTax(requestDto.getTax());
        product.setPrice(requestDto.getPrice());
        return product;
    }
}
