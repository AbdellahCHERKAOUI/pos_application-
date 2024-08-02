package com.example.posapplicationapis.dto.table;

import com.example.posapplicationapis.enums.TableStatus;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TableDtoRequest {

    @Enumerated(EnumType.STRING)
    private TableStatus reserved;

    private String floor;
}
