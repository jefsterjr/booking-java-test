package org.job.hostfully.property.facade;

import lombok.RequiredArgsConstructor;
import org.job.hostfully.owner.model.entity.Owner;
import org.job.hostfully.owner.service.OwnerService;
import org.job.hostfully.property.model.dto.CreatePropertyDTO;
import org.job.hostfully.property.model.dto.PropertyDTO;
import org.job.hostfully.property.model.entity.Property;
import org.job.hostfully.property.model.mapper.PropertyMapper;
import org.job.hostfully.property.service.PropertyService;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class PropertyFacade {


    private final PropertyService propertyService;
    private final PropertyMapper propertyMapper;
    private final OwnerService ownerService;

    public PropertyDTO findById(Long id) {
        return propertyMapper.toDTO(propertyService.findById(id));
    }

    public List<PropertyDTO> findAll() {
        return propertyMapper.toDTO(propertyService.findAll());
    }

    public PropertyDTO create(CreatePropertyDTO dto) {

        Owner owner;
        if (dto.ownerId() != null) {
            owner = ownerService.findById(dto.ownerId());
        } else if (dto.ownerName() != null && dto.ownerEmail() != null) {
            Optional<Owner> optionalOwner = ownerService.findByNameAndEmail(dto.ownerName(), dto.ownerEmail());
            owner = optionalOwner.orElseGet(() -> ownerService.save(dto.ownerName(), dto.ownerEmail()));
        } else {
            throw new IllegalArgumentException("Property owner information must be provided");
        }
        Property property = propertyMapper.fromDTO(dto, owner);
        return propertyMapper.toDTO(propertyService.save(property));
    }
}
