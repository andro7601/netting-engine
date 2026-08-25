package com.engine.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UuidGenerator;

import java.math.BigDecimal;
import java.util.UUID;

@Table(
        name = "trades",
        indexes = {
                @Index(name = "idx_trade_FK",columnList = "request_id"),
                @Index(name = "idx_trade_selected",columnList = "selected")
        }
)
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class TradeEntity {
    @Id
    @UuidGenerator
    private UUID id;

    private String tradeName;
    private BigDecimal marginRequired;
    private BigDecimal expectedPnl;
    private boolean selected;

    @ManyToOne
    @JoinColumn(name = "request_id")
    private OptimizationRequestEntity optimizationRequestEntity;

}
