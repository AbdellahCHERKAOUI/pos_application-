package com.example.posapplicationapis.services;

import com.example.posapplicationapis.dto.category.CategoryDtoRequest;
import com.example.posapplicationapis.entities.Category;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;


public interface CategoryService {
    Category createCategory(CategoryDtoRequest category);

    List<Category> getAllCategories();

    Category updateCategory( Long categoryId);

    String deleteCategory(Long categoryId);

    Category addImage(Long categoryId, MultipartFile image) throws IOException;

    public Category updateImage(Long categoryId, MultipartFile image) throws IOException;
}
