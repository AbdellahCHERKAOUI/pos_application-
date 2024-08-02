package com.example.posapplicationapis.dto.menu;

import lombok.Data;


import java.util.List;

@Data
public class MenuDtoRequest {


    private String name;
    private List<Long> categoryIds;


}


