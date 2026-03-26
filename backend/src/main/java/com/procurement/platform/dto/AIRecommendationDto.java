package com.procurement.platform.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AIRecommendationDto {
    private Long supplierId;
    private String supplierName;
    private String category;
    private Double price;
    private Integer deliveryTime;
    private Double rating;
    private Double reliabilityScore;
    private Double aiScore;
    private String reason;
}
