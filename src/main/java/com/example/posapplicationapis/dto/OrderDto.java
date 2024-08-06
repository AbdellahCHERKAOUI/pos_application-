package com.example.posapplicationapis.dto;

import com.example.posapplicationapis.dto.orderItem.OrderItemDtoResponse;
import com.example.posapplicationapis.dto.user.UserDtoResponse;
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


    private UserDtoResponse userDtoResponse;

    private Customer customer;


    private OrderItemDtoResponse orderItemDtoResponse;

}

