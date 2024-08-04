package com.example.posapplicationapis.services.table;

import com.example.posapplicationapis.dto.table.TableDtoRequest;
import com.example.posapplicationapis.dto.table.TableDtoResponse;
import com.example.posapplicationapis.entities.Table;
import com.example.posapplicationapis.repositories.TableRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TableServiceImpl implements TableService{

    @Autowired
    private TableRepository tableRepository;

    public TableDtoResponse createTable(TableDtoRequest requestDto) {
        Table table = new Table();
        table.setReserved(requestDto.getReserved());
        table.setFloor(requestDto.getFloor());

        Table savedTable = tableRepository.save(table);
        return mapToResponse(savedTable);
    }

    public List<TableDtoResponse> getAllTables() {
        return tableRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public TableDtoResponse getTable(Long id) {
        Optional<Table> table = tableRepository.findById(id);
        return table.map(this::mapToResponse)
                .orElseThrow(() -> new RuntimeException("Table not found with id " + id));
    }

    public TableDtoResponse updateTable(Long id, TableDtoRequest requestDto) {
        Table table = tableRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Table not found with id " + id));

        table.setReserved(requestDto.getReserved());
        table.setFloor(requestDto.getFloor());

        Table updatedTable = tableRepository.save(table);
        return mapToResponse(updatedTable);
    }

    public void deleteTable(Long id) {
        Table table = tableRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Table not found with id " + id));

        tableRepository.delete(table);
    }

    public TableDtoResponse mapToResponse(Table table) {
        return TableDtoResponse.builder()
                .id(table.getId())
                .reserved(table.getReserved())
                .floor(table.getFloor())
                .build();
    }
}
