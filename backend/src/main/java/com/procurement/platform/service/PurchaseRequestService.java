package com.procurement.platform.service;

import com.procurement.platform.dto.PurchaseRequestDto;
import com.procurement.platform.entity.PurchaseRequest;
import com.procurement.platform.entity.User;
import com.procurement.platform.exception.ResourceNotFoundException;
import com.procurement.platform.repository.PurchaseRequestRepository;
import com.procurement.platform.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PurchaseRequestService {

    private final PurchaseRequestRepository requestRepository;
    private final UserRepository userRepository;

    public List<PurchaseRequestDto> getAllRequests() {
        return requestRepository.findAll().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public List<PurchaseRequestDto> getRequestsByUser(Long userId) {
        return requestRepository.findByUserId(userId).stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public PurchaseRequestDto getRequestById(Long id) {
        return toDto(requestRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Purchase request not found with id: " + id)));
    }

    public PurchaseRequestDto createRequest(PurchaseRequestDto dto, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        PurchaseRequest request = PurchaseRequest.builder()
                .user(user)
                .product(dto.getProduct())
                .category(dto.getCategory())
                .quantity(dto.getQuantity())
                .budget(dto.getBudget())
                .deliveryDate(dto.getDeliveryDate())
                .description(dto.getDescription())
                .status(dto.getStatus() != null ? dto.getStatus() : "SUBMITTED")
                .build();

        return toDto(requestRepository.save(request));
    }

    public PurchaseRequestDto updateRequestStatus(Long id, String status) {
        PurchaseRequest request = requestRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Purchase request not found"));
        request.setStatus(status);
        return toDto(requestRepository.save(request));
    }

    private PurchaseRequestDto toDto(PurchaseRequest request) {
        PurchaseRequestDto dto = new PurchaseRequestDto();
        dto.setId(request.getId());
        dto.setProduct(request.getProduct());
        dto.setCategory(request.getCategory());
        dto.setQuantity(request.getQuantity());
        dto.setBudget(request.getBudget());
        dto.setDeliveryDate(request.getDeliveryDate());
        dto.setDescription(request.getDescription());
        dto.setStatus(request.getStatus());
        dto.setUserId(request.getUser().getId());
        dto.setUserName(request.getUser().getName());
        dto.setCreatedAt(request.getCreatedAt() != null ? request.getCreatedAt().toString() : null);
        return dto;
    }
}
