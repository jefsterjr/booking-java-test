package org.job.hostfully.booking.model.repository;

import org.job.hostfully.booking.model.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;


public interface BookingRepository extends JpaRepository<Booking, Long> {

    @Query("SELECT CASE WHEN (EXISTS (" +
            "SELECT 1 FROM Block block " +
            "WHERE block.property.id = :propertyId " +
            "AND (:blockId IS NULL OR block.id != :blockId) " +
            "AND block.startDate <= :endDate AND block.endDate >= :startDate " +
            "AND block.status = 'ACTIVE')) " +
            "THEN true ELSE false END")
    boolean hasActiveBookingOverlap(LocalDate startDate, LocalDate endDate, Long propertyId, Long blockId);

}
