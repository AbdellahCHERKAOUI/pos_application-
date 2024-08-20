package com.example.posapplicationapis.dto.ingridient;

import com.example.posapplicationapis.enums.IngredientUnitType;
import jakarta.persistence.*;
import lombok.Data;


@Data
public class IngredientDtoResponse {


    private Long id;

    private String name;

    @Enumerated(EnumType.STRING)
    private IngredientUnitType unitType;

    private Double quantity;

    // Getters and Setters
}

