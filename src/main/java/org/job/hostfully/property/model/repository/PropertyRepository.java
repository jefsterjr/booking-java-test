package org.job.hostfully.property.model.repository;

import org.job.hostfully.property.model.entity.Property;
import org.springframework.data.repository.CrudRepository;

public interface PropertyRepository extends CrudRepository<Property, Long> {
}
