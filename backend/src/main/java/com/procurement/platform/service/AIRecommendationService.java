package com.procurement.platform.service;

import com.procurement.platform.dto.AIRecommendationDto;
import com.procurement.platform.entity.PurchaseRequest;
import com.procurement.platform.entity.Supplier;
import com.procurement.platform.exception.ResourceNotFoundException;
import com.procurement.platform.repository.PurchaseRequestRepository;
import com.procurement.platform.repository.SupplierRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AIRecommendationService {

    private final SupplierRepository supplierRepository;
    private final PurchaseRequestRepository requestRepository;

    // Weights for AI scoring
    private static final double RATING_WEIGHT = 25.0;
    private static final double PRICE_WEIGHT = 0.01;
    private static final double DELIVERY_WEIGHT = 2.0;
    private static final double RELIABILITY_WEIGHT = 1.5;

    public List<AIRecommendationDto> getRecommendations(Long requestId) {
        PurchaseRequest request = requestRepository.findById(requestId)
                .orElseThrow(() -> new ResourceNotFoundException("Purchase request not found with id: " + requestId));

        List<Supplier> suppliers = supplierRepository.findByCategoryIgnoreCase(request.getCategory());

        if (suppliers.isEmpty()) {
            // Fallback to all suppliers if no category match
            suppliers = supplierRepository.findAll();
        }

        return suppliers.stream()
                .map(supplier -> calculateScore(supplier, request))
                .sorted(Comparator.comparingDouble(AIRecommendationDto::getAiScore).reversed())
                .collect(Collectors.toList());
    }

    private AIRecommendationDto calculateScore(Supplier supplier, PurchaseRequest request) {
        // AI Score formula: (rating * weight) - (price * weight) + (deliverySpeed) + (reliability * weight)
        double ratingScore = (supplier.getRating() != null ? supplier.getRating() : 0) * RATING_WEIGHT;
        double priceScore = supplier.getPrice() * PRICE_WEIGHT;
        double deliverySpeed = supplier.getDeliveryTime() > 0 ? (100.0 / supplier.getDeliveryTime()) * DELIVERY_WEIGHT : 0;
        double reliabilityScore = (supplier.getReliabilityScore() != null ? supplier.getReliabilityScore() : 50) * RELIABILITY_WEIGHT / 100.0;

        // Budget fit bonus: suppliers closer to budget get a bonus
        double budgetFit = 0;
        if (request.getBudget() != null && request.getBudget() > 0) {
            double ratio = supplier.getPrice() / request.getBudget();
            if (ratio <= 1.0) {
                budgetFit = (1.0 - ratio) * 20; // Up to 20 points for being under budget
            } else {
                budgetFit = -(ratio - 1.0) * 30; // Penalty for exceeding budget
            }
        }

        double totalScore = ratingScore - priceScore + deliverySpeed + reliabilityScore + budgetFit;

        // Generate reason
        String reason = generateReason(supplier, totalScore, request);

        AIRecommendationDto dto = new AIRecommendationDto();
        dto.setSupplierId(supplier.getId());
        dto.setSupplierName(supplier.getName());
        dto.setCategory(supplier.getCategory());
        dto.setPrice(supplier.getPrice());
        dto.setDeliveryTime(supplier.getDeliveryTime());
        dto.setRating(supplier.getRating());
        dto.setReliabilityScore(supplier.getReliabilityScore());
        dto.setAiScore(Math.round(totalScore * 100.0) / 100.0);
        dto.setReason(reason);
        return dto;
    }

    private String generateReason(Supplier supplier, double score, PurchaseRequest request) {
        StringBuilder reason = new StringBuilder();

        if (supplier.getRating() != null && supplier.getRating() >= 4.0) {
            reason.append("High rating (").append(supplier.getRating()).append("/5). ");
        }
        if (supplier.getDeliveryTime() <= 3) {
            reason.append("Fast delivery (").append(supplier.getDeliveryTime()).append(" days). ");
        }
        if (request.getBudget() != null && supplier.getPrice() <= request.getBudget()) {
            reason.append("Within budget. ");
        }
        if (supplier.getReliabilityScore() != null && supplier.getReliabilityScore() >= 80) {
            reason.append("Highly reliable (").append(supplier.getReliabilityScore()).append("%). ");
        }

        if (reason.length() == 0) {
            reason.append("Suitable match based on overall scoring criteria.");
        }

        return reason.toString().trim();
    }
}
