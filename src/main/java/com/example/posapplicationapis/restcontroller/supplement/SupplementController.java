package com.example.posapplicationapis.restcontroller.supplement;

import com.example.posapplicationapis.dto.supplement.SupplementDtoRequest;
import com.example.posapplicationapis.dto.supplement.SupplementDtoResponse;
import com.example.posapplicationapis.services.supplement.SupplementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/supplement")
public class SupplementController {
    @Autowired
    private SupplementService supplementService;


    @PostMapping(value = "/add-supplement")
    public SupplementDtoResponse createSupplement(@RequestBody SupplementDtoRequest requestDto) {
        SupplementDtoResponse responseDto = supplementService.createSupplement(requestDto);
        return ResponseEntity.ok(responseDto).getBody();
    }

    @GetMapping(value = "/get-all-supplement")
    public ResponseEntity<List<SupplementDtoResponse>> getAllSupplements() {
        List<SupplementDtoResponse> responseDtos = supplementService.getAllSupplements();
        return ResponseEntity.ok(responseDtos);
    }

    @GetMapping("/get-supplement/{id}")
    public ResponseEntity<SupplementDtoResponse> getSupplement(@PathVariable Long id) {
        SupplementDtoResponse responseDto = supplementService.getSupplement(id);
        return ResponseEntity.ok(responseDto);
    }
    @PutMapping("/update-supplement/{id}")
    public ResponseEntity<SupplementDtoResponse> updateSupplement(@PathVariable Long id, @RequestBody SupplementDtoRequest requestDto) {
        SupplementDtoResponse responseDto = supplementService.updateSupplement(id, requestDto);
        return ResponseEntity.ok(responseDto);
    }

    @DeleteMapping("/delete-supplement/{id}")
    public String deleteSupplement(@PathVariable Long id) {
        return supplementService.deleteSupplement(id);

    }
}
