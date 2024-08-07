package com.example.posapplicationapis.dto;
import com.example.posapplicationapis.dto.customer.CustomerDtoResponse;
import jakarta.persistence.*;


import java.time.LocalDateTime;
import java.util.List;


public class ReservationDto {


    private Long id;


    private Table table;
    private LocalDateTime date;




    private CashierDtoResponse cashier;

    private CustomerDtoResponse customer;


    private List<Table> tables;



    // Getters and Setters
}

