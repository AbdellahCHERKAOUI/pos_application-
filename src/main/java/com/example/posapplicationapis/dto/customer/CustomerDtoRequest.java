package com.example.posapplicationapis.dto.customer;

import com.example.posapplicationapis.enums.Remise;
import com.example.posapplicationapis.enums.TarifSpecial;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Data;

@Data
public class CustomerDtoRequest {
    private String name;
    @Enumerated(EnumType.STRING)
    private TarifSpecial tarifSpecial;
    @Enumerated(EnumType.STRING)
    private Remise remise;
}
