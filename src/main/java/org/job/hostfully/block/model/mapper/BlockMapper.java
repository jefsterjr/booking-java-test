package org.job.hostfully.block.model.mapper;

import org.job.hostfully.block.model.dto.BlockDTO;
import org.job.hostfully.block.model.dto.CreateBlockDTO;
import org.job.hostfully.block.model.dto.UpdateBlockDTO;
import org.job.hostfully.block.model.entity.Block;
import org.job.hostfully.property.model.entity.Property;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValueCheckStrategy;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface BlockMapper {
    @Mapping(target = "status", constant = "ACTIVE")
    @Mapping(target = "property", source = "property")
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    Block toEntity(CreateBlockDTO dto, Property property);

    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    void updateEntityFromDto(UpdateBlockDTO dto, @MappingTarget Block entity);

    @Mapping(source = "property.id", target = "propertyId")
    BlockDTO toDTO(Block block);

    List<BlockDTO> toDTO(List<Block> block);
}
