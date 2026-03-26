package com.procurement.platform.repository;

import com.procurement.platform.entity.Supplier;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface SupplierRepository extends JpaRepository<Supplier, Long> {
    List<Supplier> findByCategory(String category);
    List<Supplier> findByCategoryIgnoreCase(String category);
}
