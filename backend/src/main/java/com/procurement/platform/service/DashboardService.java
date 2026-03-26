package com.procurement.platform.service;

import com.procurement.platform.dto.DashboardDto;
import com.procurement.platform.repository.OrderRepository;
import com.procurement.platform.repository.PurchaseRequestRepository;
import com.procurement.platform.repository.SupplierRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final PurchaseRequestRepository requestRepository;
    private final OrderRepository orderRepository;
    private final SupplierRepository supplierRepository;

    public DashboardDto getDashboardStats() {
        long totalRequests = requestRepository.count();
        long totalOrders = orderRepository.count();
        long activeSuppliers = supplierRepository.count();
        long pendingApprovals = requestRepository.countByStatus("SUBMITTED");

        return new DashboardDto(totalRequests, totalOrders, activeSuppliers, pendingApprovals);
    }
}
