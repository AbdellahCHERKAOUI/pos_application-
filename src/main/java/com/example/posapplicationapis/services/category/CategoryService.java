package com.example.posapplicationapis.services.category;

import com.example.posapplicationapis.dto.category.CategoryDtoRequest;
import com.example.posapplicationapis.dto.category.CategoryDtoResponse;
import com.example.posapplicationapis.entities.Category;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;


public interface CategoryService {
    CategoryDtoResponse createCategory(CategoryDtoRequest category);

    List<CategoryDtoResponse> getAllCategories();

    CategoryDtoResponse updateCategory(Long categoryId, CategoryDtoRequest categoryDtoRequest);

    String deleteCategory(Long categoryId);

    CategoryDtoResponse addImage(Long categoryId, MultipartFile image) throws IOException;

    public CategoryDtoResponse updateImage(Long categoryId, MultipartFile image) throws IOException;

    List<CategoryDtoResponse> getAll();
}
