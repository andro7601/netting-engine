package com.engine.entity;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
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
    private long marginRequired;
    private long expectedPnl;
}
