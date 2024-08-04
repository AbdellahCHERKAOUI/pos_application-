package com.example.posapplicationapis.dto;
import jakarta.persistence.*;


import java.time.LocalDateTime;
import java.util.List;


public class ReservationDto {


    private Long id;


    private Table table;
    private LocalDateTime date;




    private CashierDtoResponse cashier;

    private CustomerDto customer;


    private List<Table> tables;



    // Getters and Setters
}

