package com.example.posapplicationapis.dto;

import com.example.posapplicationapis.entities.Order;

import java.util.List;


public class OrderItemDto {


    private Long id;


    private Order order;


    private Integer quantity;

    private Double price;


    private List<ProductDto> orderedProductDto;

}
