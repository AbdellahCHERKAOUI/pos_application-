package com.example.posapplicationapis.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
public class Supplement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToMany(mappedBy = "supplements")
    private List<Product> products;

    private double price;

    private int quantity;

    // Getters and Setters
}

