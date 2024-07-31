package com.example.posapplicationapis.dto;

import com.example.posapplicationapis.enums.PaymentMethod;
import jakarta.persistence.*;


public class PaymentDto {


    private Long id;

    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;

}

