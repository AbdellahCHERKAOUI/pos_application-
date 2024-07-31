package com.example.posapplicationapis.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
public class ProductIngredient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

   private double quantity;

    @OneToMany
    @JoinColumn(name = "ingredient_id")
    private List<Ingredient> ingredients;

    // Getters and Setters
}

