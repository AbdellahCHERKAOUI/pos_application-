package com.example.posapplicationapis.services.supplement;

import com.example.posapplicationapis.dto.supplement.SupplementDtoRequest;
import com.example.posapplicationapis.dto.supplement.SupplementDtoResponse;
import com.example.posapplicationapis.entities.Supplement;

import java.util.List;

public interface SupplementService {
    SupplementDtoResponse createSupplement(SupplementDtoRequest requestDto);
    List<SupplementDtoResponse> getAllSupplements();
    SupplementDtoResponse getSupplement(Long id);
    SupplementDtoResponse updateSupplement(Long id, SupplementDtoRequest requestDto);
    String deleteSupplement(Long id);
}
