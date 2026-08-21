package com.engine.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UuidGenerator;
import org.springframework.data.jpa.repository.EntityGraph;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "optimization_requests")
@Data
public class OptimizationRequestEntity {

    @Id
    @UuidGenerator
    private UUID id;

    private int maxMargin;
    private int totalMarginUsed;
    private int totalExpectedPnl;

    @CreationTimestamp
    @Column(updatable = false)
    private Instant createdAt;

    @OneToMany(mappedBy = "optimizationRequestEntity")
    private List<TradeEntity> tradeEntities;


}