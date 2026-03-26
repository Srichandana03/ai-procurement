package com.procurement.platform.service;

import com.procurement.platform.dto.SupplierDto;
import com.procurement.platform.entity.Supplier;
import com.procurement.platform.exception.ResourceNotFoundException;
import com.procurement.platform.repository.SupplierRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SupplierService {

    private final SupplierRepository supplierRepository;

    public List<SupplierDto> getAllSuppliers() {
        return supplierRepository.findAll().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public SupplierDto getSupplierById(Long id) {
        return toDto(supplierRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Supplier not found with id: " + id)));
    }

    public List<SupplierDto> getSuppliersByCategory(String category) {
        return supplierRepository.findByCategoryIgnoreCase(category).stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public SupplierDto createSupplier(SupplierDto dto) {
        Supplier supplier = Supplier.builder()
                .name(dto.getName())
                .category(dto.getCategory())
                .price(dto.getPrice())
                .deliveryTime(dto.getDeliveryTime())
                .rating(dto.getRating() != null ? dto.getRating() : 0.0)
                .reliabilityScore(dto.getReliabilityScore() != null ? dto.getReliabilityScore() : 50.0)
                .build();
        return toDto(supplierRepository.save(supplier));
    }

    public SupplierDto updateSupplier(Long id, SupplierDto dto) {
        Supplier supplier = supplierRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Supplier not found with id: " + id));

        supplier.setName(dto.getName());
        supplier.setCategory(dto.getCategory());
        supplier.setPrice(dto.getPrice());
        supplier.setDeliveryTime(dto.getDeliveryTime());
        if (dto.getRating() != null) supplier.setRating(dto.getRating());
        if (dto.getReliabilityScore() != null) supplier.setReliabilityScore(dto.getReliabilityScore());

        return toDto(supplierRepository.save(supplier));
    }

    public void deleteSupplier(Long id) {
        if (!supplierRepository.existsById(id)) {
            throw new ResourceNotFoundException("Supplier not found with id: " + id);
        }
        supplierRepository.deleteById(id);
    }

    private SupplierDto toDto(Supplier supplier) {
        SupplierDto dto = new SupplierDto();
        dto.setId(supplier.getId());
        dto.setName(supplier.getName());
        dto.setCategory(supplier.getCategory());
        dto.setPrice(supplier.getPrice());
        dto.setDeliveryTime(supplier.getDeliveryTime());
        dto.setRating(supplier.getRating());
        dto.setReliabilityScore(supplier.getReliabilityScore());
        return dto;
    }
}
