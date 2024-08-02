package com.example.posapplicationapis.dto.supplement;

import com.example.posapplicationapis.dto.ProductDto;
import lombok.Data;

import java.util.List;
@Data
public class SupplementDtoResponse {
    private Long id;
    private double price;
    private int quantity;
    private List<ProductDto> products;
}
