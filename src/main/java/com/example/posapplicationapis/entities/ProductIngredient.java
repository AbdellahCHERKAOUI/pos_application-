package com.example.posapplicationapis.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Entity
@Getter
@Setter
public class ProductIngredient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ElementCollection
    @CollectionTable(name = "product_ingredient_quantities", joinColumns = @JoinColumn(name = "product_ingredient_id"))
    @MapKeyJoinColumn(name = "ingredient_id")
    @Column(name = "quantity")
    private Map<Ingredient, Double> ingredientQuantities = new HashMap<>();
}

