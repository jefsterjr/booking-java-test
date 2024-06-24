package org.job.hostfully.property.service;

import org.job.hostfully.common.service.CrudService;
import org.job.hostfully.property.model.entity.Property;

import java.util.List;

public interface PropertyService extends CrudService<Property> {

    List<Property> findAll();
}
