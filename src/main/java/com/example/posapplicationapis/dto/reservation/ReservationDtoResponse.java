package com.example.posapplicationapis.dto.reservation;
import com.example.posapplicationapis.dto.CashierDtoResponse;
import com.example.posapplicationapis.dto.customer.CustomerDtoResponse;
import com.example.posapplicationapis.entities.Customer;
import jakarta.persistence.*;
import lombok.Data;


import java.time.LocalDateTime;
import java.util.List;

@Data

public class ReservationDtoResponse {


    private Long id;
    private LocalDateTime date;




    private CashierDtoResponse cashier;

    private Customer customer;


    private List<Long> tables;



    // Getters and Setters
}

