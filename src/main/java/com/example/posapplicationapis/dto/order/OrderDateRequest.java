package com.example.posapplicationapis.dto.order;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class OrderDateRequest {
    LocalDateTime date;
}
