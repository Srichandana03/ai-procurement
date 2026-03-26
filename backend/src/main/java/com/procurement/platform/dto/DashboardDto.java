package com.procurement.platform.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DashboardDto {
    private long totalRequests;
    private long totalOrders;
    private long activeSuppliers;
    private long pendingApprovals;
}
