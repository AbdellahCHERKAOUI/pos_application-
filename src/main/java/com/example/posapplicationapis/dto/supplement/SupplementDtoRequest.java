package com.example.posapplicationapis.dto.supplement;


import lombok.Data;

import java.util.List;

@Data
public class SupplementDtoRequest {
    private String name;
    private double price;
    private int quantity;


}

