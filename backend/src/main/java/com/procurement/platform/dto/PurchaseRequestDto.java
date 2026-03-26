package com.procurement.platform.dto;

import jakarta.validation.constraints.*;
import lombok.Data;
import java.time.LocalDate;

@Data
public class PurchaseRequestDto {
    private Long id;

    @NotBlank(message = "Product name is required")
    private String product;

    @NotBlank(message = "Category is required")
    private String category;

    @NotNull(message = "Quantity is required")
    @Min(value = 1, message = "Quantity must be at least 1")
    private Integer quantity;

    @NotNull(message = "Budget is required")
    @Positive(message = "Budget must be positive")
    private Double budget;

    private LocalDate deliveryDate;
    private String description;
    private String status;
    private Long userId;
    private String userName;
    private String createdAt;
}
