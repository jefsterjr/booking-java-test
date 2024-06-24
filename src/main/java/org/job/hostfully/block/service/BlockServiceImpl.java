package org.job.hostfully.block.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.job.hostfully.block.model.entity.Block;
import org.job.hostfully.block.model.repository.BlockRepository;
import org.job.hostfully.common.service.CrudService;
import org.job.hostfully.common.util.exceptions.NotFoundException;
import org.job.hostfully.common.util.exceptions.OverlapException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class BlockServiceImpl implements CrudService<Block> {
    private final BlockRepository blockRepository;

    @Override
    @Transactional
    public Block save(Block block) {
        log.info("Saving block for propertyId: {}", block.getProperty().getId());
        hasOverlapBlock(block.getProperty().getId(), block.getId(), block.getStartDate(), block.getEndDate());
        return blockRepository.save(block);
    }

    @Override
    public List<Block> findAll() {
        log.info("Finding all blocks");
        return blockRepository.findAll();
    }

    @Override
    @Transactional
    public void delete(Long id) {
        log.info("Deleting block with id: {}", id);
        blockRepository.deleteById(id);
    }

    @Override
    public Block findById(Long blockId) {
        log.info("Finding block with id: {}", blockId);
        return blockRepository.findById(blockId).orElseThrow(() -> new NotFoundException("Block not found with ID: " + blockId));
    }

    private void hasOverlapBlock(Long propertyId, Long blockId, LocalDate startDate, LocalDate endDate) {
        log.info("Checking for overlap with propertyId: {} and blockId: {}", propertyId, blockId);
        boolean hasOverlap = blockRepository.hasActiveBlockOverlap(startDate, endDate, propertyId, blockId);
        if (hasOverlap) {
            throw new OverlapException("Date overlaps found with another active block");
        }
    }
}
