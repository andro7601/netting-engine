package com.engine.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UuidGenerator;

import java.time.Instant;
import java.util.List;
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
    private Instant createdAt;

    @OneToMany
    private List<TradeEntity> tradeEntities;
}