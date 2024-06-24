package org.job.hostfully.block.model.repository;

import org.job.hostfully.block.model.entity.Block;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;

public interface BlockRepository extends JpaRepository<Block, Long> {

    @Query("SELECT CASE WHEN (EXISTS (" +
            "SELECT 1 FROM Block block " +
            "WHERE block.property.id = :propertyId " +
            "AND (:blockId IS NULL OR block.id != :blockId) " +
            "AND block.startDate <= :endDate AND block.endDate >= :startDate " +
            "AND block.status = 'ACTIVE')) " +
            "THEN true ELSE false END")
    boolean hasActiveBlockOverlap(LocalDate startDate, LocalDate endDate, Long propertyId, Long blockId);

}
