package com.example.posapplicationapis.restcontroller.menu;

import com.example.posapplicationapis.dto.menu.MenuDtoRequest;
import com.example.posapplicationapis.dto.menu.MenuDtoResponse;
import com.example.posapplicationapis.services.menu.MenuService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/menu")
public class MenuController {
    private MenuService menuService;

    public MenuController(MenuService menuService) {
        this.menuService = menuService;
    }

    @PostMapping(value = "/add-menu")
    public ResponseEntity<MenuDtoResponse> createMenu(@RequestBody MenuDtoRequest requestDto) {
        MenuDtoResponse responseDto = menuService.createMenu(requestDto);
        return ResponseEntity.ok(responseDto);
    }
    @GetMapping(value = "/get-all-menu")
    public ResponseEntity<List<MenuDtoResponse>> getAllMenus() {
        List<MenuDtoResponse> responseDtos = menuService.getAllMenus();
        return ResponseEntity.ok(responseDtos);
    }

    @GetMapping("/get-menu/{id}")
    public ResponseEntity<MenuDtoResponse> getMenu(@PathVariable Long id) {
        MenuDtoResponse responseDto = menuService.getMenu(id);
        return ResponseEntity.ok(responseDto);
    }

    @PutMapping("/update-menu/{id}")
    public ResponseEntity<MenuDtoResponse> updateMenu(@PathVariable Long id, @RequestBody MenuDtoRequest requestDto) {
        MenuDtoResponse responseDto = menuService.updateMenu(id, requestDto);
        return ResponseEntity.ok(responseDto);
    }

    @DeleteMapping("/delete-menu/{id}")
    public ResponseEntity<String> deleteMenu(@PathVariable Long id) {
        String message = menuService.deleteMenu(id);
        return ResponseEntity.ok(message);
    }
}
