package com.example.posapplicationapis.dto.supplement;

import com.example.posapplicationapis.dto.product.ProductDtoResponse;
import lombok.Data;

import java.util.List;
@Data
public class SupplementDtoResponse {
    private Long id;
    private String name;
    private double price;
    private int quantity;
    private List<ProductDtoResponse> products;
}
