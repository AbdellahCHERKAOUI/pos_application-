package com.example.posapplicationapis.services.table;

import com.example.posapplicationapis.dto.table.TableDtoRequest;
import com.example.posapplicationapis.dto.table.TableDtoResponse;
import com.example.posapplicationapis.entities.Table;

import java.util.List;

 interface TableService {
     TableDtoResponse createTable(TableDtoRequest requestDto);

     List<TableDtoResponse> getAllTables();

     TableDtoResponse getTable(Long id);

     TableDtoResponse updateTable(Long id, TableDtoRequest requestDto);

     void deleteTable(Long id);

     TableDtoResponse mapToResponse(Table table);

}
