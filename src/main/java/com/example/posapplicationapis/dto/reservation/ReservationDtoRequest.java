package com.example.posapplicationapis.dto.reservation;

import com.example.posapplicationapis.dto.CashierDtoResponse;
import com.example.posapplicationapis.dto.customer.CustomerDtoResponse;
import jakarta.persistence.Table;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class ReservationDtoRequest {


    private LocalDateTime date;

    private List<Long> tables;
}
