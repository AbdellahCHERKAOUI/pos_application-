package com.example.posapplicationapis.dto.supplement;


import lombok.Data;

import java.util.List;

@Data
public class SupplementDtoRequest {

    private double price;
    private int quantity;
    private List<Long> productIds;

}

