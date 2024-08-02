package com.example.posapplicationapis.services.supplement;

import com.example.posapplicationapis.dto.supplement.SupplementDtoRequest;
import com.example.posapplicationapis.dto.supplement.SupplementDtoResponse;
import com.example.posapplicationapis.entities.Supplement;
import com.example.posapplicationapis.repositories.ProductRepository;
import com.example.posapplicationapis.repositories.SupplementRepository;
import jakarta.annotation.PostConstruct;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SupplementServiceImpl implements SupplementService{
    @Autowired
    private SupplementRepository supplementRepository;
    @Autowired
    private ModelMapper modelMapper;


    @Override
    public SupplementDtoResponse createSupplement(SupplementDtoRequest requestDto) {
        Supplement supplement = modelMapper.map(requestDto, Supplement.class);
        /*supplement.setProducts(
                requestDto.getProductIds().stream()
                        .map(id -> productRepository.findById(id).orElseThrow(() -> new RuntimeException("Product not found")))
                        .collect(Collectors.toList())
        );*/
        Supplement savedSupplement = supplementRepository.save(supplement);
        return modelMapper.map(savedSupplement, SupplementDtoResponse.class);
    }

    @Override
    public List<SupplementDtoResponse> getAllSupplements() {
        return supplementRepository.findAll().stream()
                .map(supplement -> modelMapper.map(supplement, SupplementDtoResponse.class))
                .collect(Collectors.toList());
    }

    @Override
    public SupplementDtoResponse getSupplement(Long id) {
        Supplement supplement = supplementRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Supplement not found"));
        return modelMapper.map(supplement, SupplementDtoResponse.class);
    }

    @Override
    public SupplementDtoResponse updateSupplement(Long id, SupplementDtoRequest requestDto) {
        Supplement supplement = supplementRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Supplement not found"));

        modelMapper.map(requestDto, supplement);
       /* supplement.setProducts(
                requestDto.getProductIds().stream()
                        .map(productId -> productRepository.findById(productId).orElseThrow(() -> new RuntimeException("Product not found")))
                        .collect(Collectors.toList())
        );*/

        Supplement updatedSupplement = supplementRepository.save(supplement);
        return modelMapper.map(updatedSupplement, SupplementDtoResponse.class);
    }

    @Override
    public String deleteSupplement(Long id) {
        Supplement supplement = supplementRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Supplement not found"));
        supplementRepository.delete(supplement);
        return "Supplement deleted successfully";
    }
}
