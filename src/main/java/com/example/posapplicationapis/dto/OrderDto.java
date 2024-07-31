package com.example.posapplicationapis.dto;

import com.example.posapplicationapis.entities.Customer;
import com.example.posapplicationapis.enums.OrderStatus;
import jakarta.persistence.*;


import java.time.LocalDateTime;


public class OrderDto {

    private Long id;

    private LocalDateTime date;

    private String receiptNumber;

    private Double total;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;


    private PaymentDto paymentDto;


    private UserDto userDto;

    private Customer customer;


    private OrderItemDto orderItemDto;

}
