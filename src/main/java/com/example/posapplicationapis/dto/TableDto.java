package com.example.posapplicationapis.dto;

import com.example.posapplicationapis.enums.TableStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;


public class TableDto {

    private Long id;

    @Enumerated(EnumType.STRING)
    private TableStatus reserved;

    private String floor;

}

