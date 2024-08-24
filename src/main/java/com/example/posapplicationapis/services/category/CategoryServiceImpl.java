package com.example.posapplicationapis.services.category;

import com.example.posapplicationapis.dto.category.CategoryDtoRequest;
import com.example.posapplicationapis.dto.category.CategoryDtoResponse;
import com.example.posapplicationapis.entities.Category;
import com.example.posapplicationapis.entities.Image;
import com.example.posapplicationapis.entities.Product;
import com.example.posapplicationapis.repositories.CategoryRepository;
import com.example.posapplicationapis.repositories.ImageRepository;
import com.example.posapplicationapis.service.ImageService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
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

    private CategoryDtoResponse mapToDto(Category category) {
        CategoryDtoResponse dto = new CategoryDtoResponse();
        dto.setId(category.getId());
        dto.setName(category.getName());
        if (category.getImage() != null) {
            dto.setImageLink(category.getImage().getLink());
        } else {
            dto.setImageLink(null);
        }
        return dto;
    }

    @Override
    public CategoryDtoResponse createCategory(CategoryDtoRequest categoryDtoRequest) {
        Category category = new Category();
        category.setName(categoryDtoRequest.getName());
        Category savedCategory = categoryRepository.save(category);
        return mapToDto(savedCategory);
    }

    @Override
    public List<CategoryDtoResponse> getAllCategories() {
        return List.of();
    }

//    @Override
//    public List<CategoryDtoResponse> getAllCategories() {
//        List<Category> categories = categoryRepository.findAll();
//        return categories.stream().map(this::mapToDto).toList();
//    }

    @Override
    public CategoryDtoResponse updateCategory(Long categoryId, CategoryDtoRequest categoryDtoRequest) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new RuntimeException("Category not found"));

        category.setName(categoryDtoRequest.getName());
        Category updatedCategory = categoryRepository.save(category);
        return mapToDto(updatedCategory);
    }

    @Override
    @Transactional
    public String deleteCategory(Long categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new RuntimeException("Category not found"));
        Image oldImage = category.getImage();
        category.setImage(null);
        if(oldImage != null){
            imageRepository.delete(oldImage);
        }
        for (Product product : category.getProducts()){
            product.setCategory(null);
        }
        category.setProducts(null);
        category.setMenus(null);

        categoryRepository.delete(category);
        return "Category deleted successfully";
    }

    @Override
    public CategoryDtoResponse addImage(Long categoryId, MultipartFile image) throws IOException {
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
        Category updatedCategory = categoryRepository.save(category);
        return mapToDto(updatedCategory);
    }

    @Override
    public CategoryDtoResponse updateImage(Long categoryId, MultipartFile image) throws IOException {
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
        Category updatedCategory = categoryRepository.save(category);
        return mapToDto(updatedCategory);
    }

    @Override
    public List<CategoryDtoResponse> getAll() {

        List<CategoryDtoResponse> categoryDtoResponses = new ArrayList<>();

        for (Category category : categoryRepository.findAll())     {
            categoryDtoResponses.add(mapToDto(category));
        }

        return categoryDtoResponses;
    }

    @Override
    public CategoryDtoResponse getByName(String name) {
        return mapToDto(categoryRepository.findByName(name));
    }
}
