package com.example.posapplicationapis.services;

import com.example.posapplicationapis.entities.Category;
import org.springframework.stereotype.Service;

@Service
public interface CategoryService {
    Category createCategory(Category category);

    Category getAllCategories();

    Category updateCategory( Long categoryId);

    String deleteCategory(Long categoryId);
}
