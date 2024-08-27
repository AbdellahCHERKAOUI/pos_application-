package com.example.posapplicationapis.dto.menu;

import com.example.posapplicationapis.dto.category.CategoryDtoResponse;
import lombok.Data;

import java.util.List;
@Data
public class MenuDtoResponse {
    private Long id;
    private String name;
    private List<CategoryDtoResponse> categories;
}
