package com.example.posapplicationapis.entities;

import com.example.posapplicationapis.enums.IngredientUnitType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Ingredient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Enumerated(EnumType.STRING)
    private IngredientUnitType unitType;

    // Getters and Setters
}

