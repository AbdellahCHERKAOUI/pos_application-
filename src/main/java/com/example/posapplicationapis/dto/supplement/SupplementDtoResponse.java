package com.example.posapplicationapis.dto.supplement;

import lombok.Data;

@Data
public class SupplementDtoResponse {
    private Long id;
    private String name;
    private double price;
    private int quantity;
}
