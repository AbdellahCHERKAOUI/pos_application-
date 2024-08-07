package com.example.posapplicationapis.dto.customer;

import com.example.posapplicationapis.enums.Remise;
import com.example.posapplicationapis.enums.TarifSpecial;
import jakarta.persistence.*;
import lombok.Data;


@Data
public class CustomerDtoResponse {
    private Long id;
    private String name;
    @Enumerated(EnumType.STRING)
    private TarifSpecial tarifSpecial;
    @Enumerated(EnumType.STRING)
    private Remise remise;

}

