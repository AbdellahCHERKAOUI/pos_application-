package com.example.posapplicationapis.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Entity
@Getter
@Setter
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private Double salesPrice;
    private Double vipPrice;
    private Double tax;
    private Double price;
    @OneToOne(cascade = CascadeType.REMOVE)
    @ToString.Exclude
    private Image image;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @OneToMany

    private List<ProductIngredient> ingredients;

    @ManyToMany
    @JoinTable(
            name = "product_supplement",
            joinColumns = @JoinColumn(name = "product_id"),
            inverseJoinColumns = @JoinColumn(name = "supplement_id")
    )
    private List<Supplement> supplements;



    // Getters and Setters
}

