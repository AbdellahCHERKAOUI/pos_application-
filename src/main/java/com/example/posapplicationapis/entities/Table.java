package com.example.posapplicationapis.entities;

import com.example.posapplicationapis.enums.TableStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity(name = "RestaurantTable") // "Table" is a reserved keyword in SQL
@Getter
@Setter
public class Table {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private TableStatus reserved;

    private String floor;

    // Getters and Setters
}

