package com.procurement.platform.repository;

import com.procurement.platform.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByStatus(String status);
    long countByStatus(String status);
}
