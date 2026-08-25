package com.engine.repository;

import com.engine.entity.OptimizationRequestEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface OptimizationRequestRepository extends JpaRepository<OptimizationRequestEntity, UUID> {

    @EntityGraph(attributePaths = "tradeEntities")
    Optional<OptimizationRequestEntity> findById(UUID id);//override

    @EntityGraph(attributePaths = "tradeEntities")
    List<OptimizationRequestEntity> findAllByOrderByCreatedAtDesc(Pageable pageable);
}
