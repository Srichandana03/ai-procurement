package com.procurement.platform.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "suppliers")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Supplier {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 150)
    private String name;

    @Column(nullable = false, length = 100)
    private String category;

    @Column(nullable = false)
    private Double price;

    @Column(name = "delivery_time", nullable = false)
    private Integer deliveryTime;

    @Column
    private Double rating;

    @Column(name = "reliability_score")
    private Double reliabilityScore;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        if (rating == null) rating = 0.0;
        if (reliabilityScore == null) reliabilityScore = 50.0;
    }
}
