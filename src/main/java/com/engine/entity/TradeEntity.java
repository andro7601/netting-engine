package com.engine.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UuidGenerator;

import java.util.UUID;

@Table(name = "trades")
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class TradeEntity {
    @Id
    @UuidGenerator
    private UUID id;

    private String tradeName;
    private int marginRequired;
    private int expectedPnl;
    private boolean selected;

    @ManyToOne
    @JoinColumn(name = "request_id")
    private OptimizationRequestEntity optimizationRequestEntity;

}
