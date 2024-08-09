package com.example.posapplicationapis.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Entity
@Getter
@Setter
public class Stock {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ElementCollection
    @CollectionTable(name = "stock_ingredient_mapping",
            joinColumns = @JoinColumn(name = "stock_id"))
    @MapKeyJoinColumn(name = "ingredient_id")
    @Column(name = "quantity")
    private Map<Ingredient, Double> ingredientStockMap;

    // Getters and Setters
}

