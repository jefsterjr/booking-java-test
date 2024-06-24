package org.job.hostfully.owner.model.repository;

import org.job.hostfully.owner.model.entity.Owner;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OwnerRepository extends JpaRepository<Owner, Long> {

    Optional<Owner> findTopByNameAndEmail(String name, String email);
}
