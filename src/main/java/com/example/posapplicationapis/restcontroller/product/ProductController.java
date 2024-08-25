package com.example.posapplicationapis.restcontroller.product;

import com.example.posapplicationapis.dto.product.ProductDtoRequest;
import com.example.posapplicationapis.dto.product.ProductDtoResponse;
import com.example.posapplicationapis.services.product.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping(value = "/product")
public class ProductController {
    @Autowired
    private ProductService productService;

    @PostMapping("/add-product")
    public ResponseEntity<ProductDtoResponse> createProduct(@RequestBody ProductDtoRequest requestDto) {
        ProductDtoResponse responseDto = productService.createProduct(requestDto);
        return ResponseEntity.ok(responseDto);
    }

    @GetMapping("/get-all-product")
    public ResponseEntity<List<ProductDtoResponse>> getAllProducts() {
        List<ProductDtoResponse> responseDtos = productService.getAllProducts();
        return ResponseEntity.ok(responseDtos);
    }
    @GetMapping("/{categoryId}/get-all-product-by-category")
    public ResponseEntity<List<ProductDtoResponse>> getAllProductByCategory(@PathVariable Long categoryId) {
        List<ProductDtoResponse> responseDtos = productService.getAllProductsByCategory(categoryId);
        return ResponseEntity.ok(responseDtos);
    }

    @GetMapping("/get-product/{id}")
    public ResponseEntity<ProductDtoResponse> getProduct(@PathVariable Long id) {
        ProductDtoResponse responseDto = productService.getProduct(id);
        return ResponseEntity.ok(responseDto);
    }

    @PutMapping("/update-product/{id}")
    public ResponseEntity<ProductDtoResponse> updateProduct(@PathVariable Long id, @RequestBody ProductDtoRequest requestDto) {
        ProductDtoResponse responseDto = productService.updateProduct(id, requestDto);
        return ResponseEntity.ok(responseDto);
    }

    @DeleteMapping("/delete-product/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable Long id) {
        String message = productService.deleteProduct(id);
        return ResponseEntity.ok(message);
    }

    @PutMapping("/add-image-product/{id}")
    public ResponseEntity<ProductDtoResponse> addImageToProduct(@PathVariable Long id, @RequestBody MultipartFile image) throws IOException {
        ProductDtoResponse responseDto = productService.addImage(id, image);
        return ResponseEntity.ok(responseDto);
    }
}
