package com.example.posapplicationapis.restcontroller.Category;


import com.example.posapplicationapis.dto.category.CategoryDtoRequest;
import com.example.posapplicationapis.entities.Category;
import com.example.posapplicationapis.repositories.CategoryRepository;
import com.example.posapplicationapis.service.ImageService;
import com.example.posapplicationapis.services.CategoryService;
import com.example.posapplicationapis.services.CategoryServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/category")
public class CategoryController {
    CategoryServiceImpl categoryService;

    public CategoryController(CategoryServiceImpl categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping("/add-category")
    private Category addCategory(@RequestBody CategoryDtoRequest categoryDtoRequest){
        return categoryService.createCategory(categoryDtoRequest);
    }

    @PutMapping("/{id}/update-category")
    private Category updateCategory(@PathVariable Long id,@RequestBody CategoryDtoRequest categoryDtoRequest){
        return categoryService.updateCategory(id, categoryDtoRequest);
    }

    @DeleteMapping("/{id}/remove-category")
    private String removeCategory(@PathVariable Long id){
        return categoryService.deleteCategory(id);
    }

    @PostMapping("/{categoryId}/add-category-image")
    private Category addImageCategory(@PathVariable Long categoryId, @RequestBody MultipartFile image) throws IOException {
        return categoryService.addImage(categoryId,image);
    }

    @PostMapping("/{categoryId}/update-category-image")
    private Category updateImageCategory(@PathVariable Long categoryId, @RequestBody MultipartFile image) throws IOException {
        return categoryService.updateImage(categoryId,image);
    }


}
