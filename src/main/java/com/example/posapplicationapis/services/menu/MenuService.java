package com.example.posapplicationapis.services.menu;

import com.example.posapplicationapis.dto.menu.MenuDtoRequest;
import com.example.posapplicationapis.dto.menu.MenuDtoResponse;

import java.util.List;

public interface MenuService {
    MenuDtoResponse createMenu(MenuDtoRequest requestDto);
    List<MenuDtoResponse> getAllMenus();
    MenuDtoResponse getMenu(Long id);
    MenuDtoResponse updateMenu(Long id, MenuDtoRequest requestDto);
    String deleteMenu(Long id);
}
