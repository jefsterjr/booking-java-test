package org.job.hostfully.block.facade;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.job.hostfully.block.model.dto.BlockDTO;
import org.job.hostfully.block.model.dto.CreateBlockDTO;
import org.job.hostfully.block.model.dto.UpdateBlockDTO;
import org.job.hostfully.block.model.entity.Block;
import org.job.hostfully.block.model.mapper.BlockMapper;
import org.job.hostfully.booking.service.BookingService;
import org.job.hostfully.common.service.CrudService;
import org.job.hostfully.property.model.entity.Property;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class BlockFacade {

    private final CrudService<Property> propertyService;
    private final BlockMapper blockMapper;
    private final CrudService<Block> blockService;

    public BlockDTO create(CreateBlockDTO dto) {
        log.info("Creating block for propertyId: {}", dto.propertyId());
        Property property = propertyService.findById(dto.propertyId());
        Block block = blockMapper.toEntity(dto, property);
        Block save = blockService.save(block);
        return blockMapper.toDTO(save);
    }

    public BlockDTO update(Long blockId, UpdateBlockDTO dto) {
        log.info("Updating block with id: {}", blockId);
        Block block = blockService.findById(blockId);
        blockMapper.updateEntityFromDto(dto, block);
        return blockMapper.toDTO(blockService.save(block));
    }

    public BlockDTO findById(Long blockId) {
        log.info("Finding block with id: {}", blockId);
        return blockMapper.toDTO(blockService.findById(blockId));
    }

    public void delete(Long blockId) {
        log.info("Deleting block with id: {}", blockId);
        blockService.delete(blockId);
    }

    public List<BlockDTO> findAll() {
        log.info("Finding all blocks");
        return blockMapper.toDTO(blockService.findAll());
    }
}
