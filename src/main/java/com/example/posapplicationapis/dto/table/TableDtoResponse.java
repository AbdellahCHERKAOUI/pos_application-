package com.example.posapplicationapis.dto.table;

import com.example.posapplicationapis.enums.TableStatus;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TableDtoResponse {

    private Long id;

    @Enumerated(EnumType.STRING)
    private TableStatus reserved;

    private String floor;

}

