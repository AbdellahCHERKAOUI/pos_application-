package com.example.posapplicationapis.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Entity

@Getter
@Setter
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @OneToMany(mappedBy = "category", cascade =  CascadeType.ALL )
    private List<Product> products;

    @ManyToMany(mappedBy = "categories")
    private List<Menu> menus;
    @OneToOne
    @JoinColumn(name = "category_photo_id")
    private Image image;
    // Add other fields as necessary

    // Getters and Setters
}

