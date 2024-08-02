package com.example.posapplicationapis.entities;

import com.example.posapplicationapis.enums.Remise;
import com.example.posapplicationapis.enums.TarifSpecial;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Customer {
    @Id @GeneratedValue
    private long id;
    private String name;
    @Enumerated(EnumType.STRING)
    private TarifSpecial tarifSpecial;
    @Enumerated(EnumType.STRING)
    private Remise remise;

}
