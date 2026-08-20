package com.engine.repository;

import com.engine.entity.OptimizationRequestEntity;
import com.engine.entity.TradeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface OptimizationRequestRepository extends JpaRepository<OptimizationRequestEntity, UUID> {
}
