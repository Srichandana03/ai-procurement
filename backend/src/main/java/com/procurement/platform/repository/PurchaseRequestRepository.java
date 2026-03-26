package com.procurement.platform.repository;

import com.procurement.platform.entity.PurchaseRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PurchaseRequestRepository extends JpaRepository<PurchaseRequest, Long> {
    List<PurchaseRequest> findByUserId(Long userId);
    List<PurchaseRequest> findByStatus(String status);
    long countByStatus(String status);
}
