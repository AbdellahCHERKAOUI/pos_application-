package com.example.posapplicationapis.dto.reservation;
import com.example.posapplicationapis.dto.CashierDtoResponse;
import com.example.posapplicationapis.dto.customer.CustomerDtoResponse;
import jakarta.persistence.*;


import java.time.LocalDateTime;
import java.util.List;


public class ReservationDtoResponse {


    private Long id;
    private LocalDateTime date;




    private CashierDtoResponse cashier;

    private CustomerDtoResponse customer;


    private List<Long> tables;



    // Getters and Setters
}

