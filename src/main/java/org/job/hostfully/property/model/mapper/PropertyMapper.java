package org.job.hostfully.property.model.mapper;

import org.job.hostfully.owner.model.entity.Owner;
import org.job.hostfully.property.model.dto.CreatePropertyDTO;
import org.job.hostfully.property.model.dto.PropertyDTO;
import org.job.hostfully.property.model.entity.Property;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.NullValueCheckStrategy;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface PropertyMapper {
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    @Mapping(target = "address", source = "address")
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    List<PropertyDTO> toDTO(List<Property> properties);

    @Mapping(target = "name", source = "dto.name")
    @Mapping(target = "address", source = "dto.address")
    @Mapping(target = "owner", source = "owner")
    Property fromDTO(CreatePropertyDTO dto, Owner owner);

    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    @Mapping(target = "address", source = "address")
    @Mapping(target = "owner", source = "owner")
    PropertyDTO toDTO(Property entity);
}
