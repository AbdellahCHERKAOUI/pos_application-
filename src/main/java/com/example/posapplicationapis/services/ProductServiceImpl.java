package com.example.posapplicationapis.services;

import com.example.posapplicationapis.entities.Image;
import com.example.posapplicationapis.entities.Product;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
@Service
public class ProductServiceImpl implements ProductService{
    @Override
    public Product addProduct(Long categoryId, Product product, Image image) throws IOException {
        return null;
    }

    @Override
    public Image saveFile(Image image) {
        return null;
    }

    @Override
    public Product updateProduct(Long productId) {
        return null;
    }

    @Override
    public String deleteProduct(Long productId) {
        return null;
    }

    @Override
    public Product getProduct(Long productId) {
        return null;
    }

    @Override
    public List<Product> getAllProducts() {
        return null;
    }
}
