package com.engine.repository;

import com.engine.entity.TradeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface TradeRepository extends JpaRepository<TradeEntity, UUID> {
}
