package org.job.hostfully.property.service.impl;

import lombok.RequiredArgsConstructor;
import org.job.hostfully.common.util.exceptions.NotFoundException;
import org.job.hostfully.property.model.entity.Property;
import org.job.hostfully.property.model.repository.PropertyRepository;
import org.job.hostfully.property.service.PropertyService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PropertyServiceImpl implements PropertyService {

    private final PropertyRepository propertyRepository;

    @Override
    public Property save(Property property) {
        return propertyRepository.save(property);
    }

    @Override
    public Property findById(Long propertyId) {
        return propertyRepository.findById(propertyId).orElseThrow(() -> new NotFoundException("Property not found"));
    }

    @Override
    public void delete(Long id) {
        propertyRepository.deleteById(id);
    }

    @Override
    public List<Property> findAll() {
        return (List<Property>) propertyRepository.findAll();
    }
}
