package com.example.posapplicationapis.dto;

import com.example.posapplicationapis.enums.IngredientUnitType;
import jakarta.persistence.*;



public class IngredientDto {


    private Long id;

    private String name;

    @Enumerated(EnumType.STRING)
    private IngredientUnitType unitType;

    // Getters and Setters
}

