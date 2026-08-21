package com.engine.repository;

import com.engine.entity.TradeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface TradeRepository extends JpaRepository<TradeEntity, UUID> {
}
