package com.procurement.platform.dto;

import lombok.Data;

@Data
public class OrderDto {
    private Long id;
    private Long requestId;
    private Long supplierId;
    private String status;
    private Double totalAmount;
    private String orderDate;
    private String productName;
    private String supplierName;
}
