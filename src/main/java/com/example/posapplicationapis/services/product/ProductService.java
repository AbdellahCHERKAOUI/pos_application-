package com.example.posapplicationapis.services.product;

import com.example.posapplicationapis.entities.Image;
import com.example.posapplicationapis.entities.Product;
import org.springframework.stereotype.Service;



import java.io.IOException;
import java.util.List;


public interface ProductService {

    Product addProduct(Long categoryId, Product product, Image image) throws IOException;


    Image saveFile(Image image);


    Product updateProduct(Long productId);


    String deleteProduct(Long productId);

    Product getProduct(Long productId);

    List<Product> getAllProducts();
}
