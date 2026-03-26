package com.procurement.platform.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class SupplierDto {
    private Long id;

    @NotBlank(message = "Supplier name is required")
    private String name;

    @NotBlank(message = "Category is required")
    private String category;

    @NotNull(message = "Price is required")
    @Positive(message = "Price must be positive")
    private Double price;

    @NotNull(message = "Delivery time is required")
    @Min(value = 1, message = "Delivery time must be at least 1 day")
    private Integer deliveryTime;

    @DecimalMin(value = "0.0") @DecimalMax(value = "5.0")
    private Double rating;

    private Double reliabilityScore;
}
