package com.example.posapplicationapis.dto.ingridient;

import com.example.posapplicationapis.enums.IngredientUnitType;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class IngredientDtoRequest {
    private String name;

    @Enumerated(EnumType.STRING)
    private IngredientUnitType unitType;
}
