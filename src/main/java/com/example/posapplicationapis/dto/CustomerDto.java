package com.example.posapplicationapis.dto;

import com.example.posapplicationapis.enums.Remise;
import com.example.posapplicationapis.enums.TarifSpecial;
import jakarta.persistence.*;


public class CustomerDto {
    private long id;
    private String name;
    @Enumerated(EnumType.STRING)
    private TarifSpecial tarifSpecial;
    @Enumerated(EnumType.STRING)
    private Remise remise;

}

