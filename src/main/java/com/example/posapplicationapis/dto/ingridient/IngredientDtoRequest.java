package com.example.posapplicationapis.dto.ingridient;

import com.example.posapplicationapis.enums.IngredientUnitType;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Data;

@Data
public class IngredientDtoRequest {
    private String name;

    @Enumerated(EnumType.STRING)
    private IngredientUnitType unitType;

    private Double quantity;
}
