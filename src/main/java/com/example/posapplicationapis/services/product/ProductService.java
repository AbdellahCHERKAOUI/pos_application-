package com.example.posapplicationapis.services.product;

import com.example.posapplicationapis.dto.product.ProductDtoRequest;
import com.example.posapplicationapis.dto.product.ProductDtoResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;


public interface ProductService {

    ProductDtoResponse createProduct(ProductDtoRequest requestDto);
    List<ProductDtoResponse> getAllProducts();
    ProductDtoResponse getProduct(Long id);
    ProductDtoResponse updateProduct(Long id, ProductDtoRequest requestDto);
    String deleteProduct(Long id);

    ProductDtoResponse addImage(Long id, MultipartFile image) throws IOException;
    List<ProductDtoResponse>getAllProductsByCategory(Long categoryId);
}
