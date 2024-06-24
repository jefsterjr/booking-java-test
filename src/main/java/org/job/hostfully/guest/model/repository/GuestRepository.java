package org.job.hostfully.guest.model.repository;

import org.job.hostfully.guest.model.entity.Guest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface GuestRepository extends JpaRepository<Guest, Long> {

    Optional<Guest> findTopByNameAndEmail(String name, String email);
}
