package com.example.posapplicationapis.services.menu;

import com.example.posapplicationapis.dto.menu.MenuDtoRequest;
import com.example.posapplicationapis.dto.menu.MenuDtoResponse;
import com.example.posapplicationapis.entities.Category;
import com.example.posapplicationapis.entities.Menu;
import com.example.posapplicationapis.repositories.CategoryRepository;
import com.example.posapplicationapis.repositories.MenuRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MenuServiceImpl implements MenuService {
    private  MenuRepository menuRepository;
    private  CategoryRepository categoryRepository;

    @Autowired
    private ModelMapper modelMapper;

    public MenuServiceImpl(MenuRepository menuRepository, CategoryRepository categoryRepository) {
        this.menuRepository = menuRepository;
        this.categoryRepository = categoryRepository;
    }

    @Override
    public MenuDtoResponse createMenu(MenuDtoRequest requestDto) {
        Menu menu = modelMapper.map(requestDto, Menu.class);

        // Fetch the categories by their IDs
        List<Category> categories = requestDto.getCategoryIds().stream()
                .map(id -> categoryRepository.findById(id)
                        .orElseThrow(() -> new RuntimeException("Category not found: " + id)))
                .collect(Collectors.toList());
        menu.setRestrictedCategories(categories);

        Menu savedMenu = menuRepository.save(menu);
        return modelMapper.map(savedMenu, MenuDtoResponse.class);
    }

    @Override
    public List<MenuDtoResponse> getAllMenus() {
        return menuRepository.findAll().stream()
                .map(menu -> modelMapper.map(menu, MenuDtoResponse.class))
                .collect(Collectors.toList());
    }

    @Override
    public MenuDtoResponse getMenu(Long id) {
        Menu menu = menuRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Menu not found"));
        return modelMapper.map(menu, MenuDtoResponse.class);
    }

    @Override
    public MenuDtoResponse updateMenu(Long id, MenuDtoRequest requestDto) {
        Menu menu = menuRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Menu not found"));

        modelMapper.map(requestDto, menu);
        List<Category> categories = requestDto.getCategoryIds().stream()
                .map(categoryId -> categoryRepository.findById(categoryId)
                        .orElseThrow(() -> new RuntimeException("Category not found: " + categoryId)))
                .collect(Collectors.toList());
        menu.setRestrictedCategories(categories);

        Menu updatedMenu = menuRepository.save(menu);
        return modelMapper.map(updatedMenu, MenuDtoResponse.class);
    }

    @Override
    public String deleteMenu(Long id) {
        Menu menu = menuRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Menu not found"));
        menuRepository.delete(menu);
        return "Menu deleted successfully";
    }
}
