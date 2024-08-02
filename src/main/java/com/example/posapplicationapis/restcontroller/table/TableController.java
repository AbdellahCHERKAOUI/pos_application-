package com.example.posapplicationapis.restcontroller.table;

import com.example.posapplicationapis.dto.table.TableDtoRequest;
import com.example.posapplicationapis.dto.table.TableDtoResponse;
import com.example.posapplicationapis.services.table.TableServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/table")
public class TableController {
    @Autowired
    private TableServiceImpl tableServiceImpl;

    @PostMapping(value = "/add-table")
    public ResponseEntity<TableDtoResponse> createTable(@RequestBody TableDtoRequest requestDto) {
        TableDtoResponse response = tableServiceImpl.createTable(requestDto);
        return ResponseEntity.ok(response);
    }

    @GetMapping(value = "/get-all-table")
    public ResponseEntity<List<TableDtoResponse>> getAllTables() {
        List<TableDtoResponse> tables = tableServiceImpl.getAllTables();
        return ResponseEntity.ok(tables);
    }

    @GetMapping("/get-table/{id}")
    public ResponseEntity<TableDtoResponse> getTable(@PathVariable Long id) {
        TableDtoResponse response = tableServiceImpl.getTable(id);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/update-table/{id}")
    public ResponseEntity<TableDtoResponse> updateTable(@PathVariable Long id, @RequestBody TableDtoRequest requestDto) {
        TableDtoResponse response = tableServiceImpl.updateTable(id, requestDto);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/delete-table/{id}")
    public ResponseEntity<String> deleteTable(@PathVariable Long id) {
        tableServiceImpl.deleteTable(id);
        return ResponseEntity.ok("Table deleted successfully");
    }
}
