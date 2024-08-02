package com.example.posapplicationapis.services.category;

import com.example.posapplicationapis.dto.category.CategoryDtoRequest;
import com.example.posapplicationapis.entities.Category;
import com.example.posapplicationapis.entities.Image;
import com.example.posapplicationapis.repositories.CategoryRepository;
import com.example.posapplicationapis.repositories.ImageRepository;
import com.example.posapplicationapis.service.ImageService;
import com.example.posapplicationapis.services.category.CategoryService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final ImageRepository imageRepository;
    private final ImageService imageService;

    public CategoryServiceImpl(CategoryRepository categoryRepository, ImageRepository imageRepository, ImageService imageService) {
        this.categoryRepository = categoryRepository;
        this.imageRepository = imageRepository;
        this.imageService = imageService;
    }

    @Override
    public Category createCategory(CategoryDtoRequest categoryDtoRequest) {
        Category category = new Category();
        category.setName(categoryDtoRequest.getName());
        return categoryRepository.save(category);
    }

    @Override
    public List<Category> getAllCategories() {
        return null;
    }

    @Override
    public Category updateCategory(Long categoryId) {
        return null;
    }


    public Category updateCategory(Long categoryId, CategoryDtoRequest categoryDtoRequest) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new RuntimeException("Category not found"));

        category.setName(categoryDtoRequest.getName());
        return categoryRepository.save(category);
    }

    @Override
    public String deleteCategory(Long categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new RuntimeException("Category not found"));
        Image oldImage = category.getImage();
        category.setImage(null);
        imageRepository.delete(oldImage);

        categoryRepository.delete(category);
        return "Category deleted successfully";
    }

    @Override
    public Category addImage(Long categoryId, MultipartFile image) throws IOException {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new RuntimeException("Category not found"));

        String link = imageService.uploadFile(image);

        if (link == null) {
            throw new RuntimeException("Failed to upload the image, link is null");
        }

        Image image1 = new Image();
        image1.setLink(link);
        imageRepository.save(image1);

        category.setImage(image1);
        return categoryRepository.save(category);
    }

    @Override
    public Category updateImage(Long categoryId, MultipartFile image) throws IOException {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new RuntimeException("Category not found"));
        Image oldImage = category.getImage();
        category.setImage(null);
        imageRepository.delete(oldImage);

        String link = imageService.uploadFile(image);

        if (link == null) {
            throw new RuntimeException("Failed to upload the image, link is null");
        }

        Image image1 = new Image();
        image1.setLink(link);
        imageRepository.save(image1);

        category.setImage(image1);
        return categoryRepository.save(category);
    }




}
