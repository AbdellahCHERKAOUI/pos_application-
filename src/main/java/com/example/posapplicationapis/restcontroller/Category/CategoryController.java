package com.example.posapplicationapis.restcontroller.Category;


import com.example.posapplicationapis.dto.category.CategoryDtoRequest;
import com.example.posapplicationapis.dto.category.CategoryDtoResponse;
import com.example.posapplicationapis.entities.Category;
import com.example.posapplicationapis.services.category.CategoryServiceImpl;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/category")
public class CategoryController {
    CategoryServiceImpl categoryService;

    public CategoryController(CategoryServiceImpl categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping("/add-category")
    private CategoryDtoResponse addCategory(@RequestBody CategoryDtoRequest categoryDtoRequest){
        return categoryService.createCategory(categoryDtoRequest);
    }

    @PutMapping("/{id}/update-category")
    private CategoryDtoResponse updateCategory(@PathVariable Long id,@RequestBody CategoryDtoRequest categoryDtoRequest){
        return categoryService.updateCategory(id, categoryDtoRequest);
    }

    @DeleteMapping("/{id}/remove-category")
    private String removeCategory(@PathVariable Long id){
        return categoryService.deleteCategory(id);
    }

    @PostMapping("/{categoryId}/add-category-image")
    private CategoryDtoResponse addImageCategory(@PathVariable Long categoryId, @RequestBody MultipartFile image) throws IOException {
        return categoryService.addImage(categoryId,image);
    }

    @PostMapping("/{categoryId}/update-category-image")
    private CategoryDtoResponse updateImageCategory(@PathVariable Long categoryId, @RequestBody MultipartFile image) throws IOException {
        return categoryService.updateImage(categoryId,image);
    }

    @GetMapping("/get-all")
    private List<CategoryDtoResponse> updateImageCategory() {
        return categoryService.getAll();
    }

    @GetMapping("/get-name")
    private CategoryDtoResponse getCategoryByName(@RequestParam String name) {
        return categoryService.getByName(name);
    }


}
