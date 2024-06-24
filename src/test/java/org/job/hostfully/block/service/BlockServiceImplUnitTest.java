package org.job.hostfully.block.service;

import org.job.hostfully.block.model.entity.Block;
import org.job.hostfully.block.model.repository.BlockRepository;
import org.job.hostfully.common.model.enums.BlockType;
import org.job.hostfully.common.model.enums.Status;
import org.job.hostfully.common.util.exceptions.NotFoundException;
import org.job.hostfully.common.util.exceptions.OverlapException;
import org.job.hostfully.owner.model.entity.Owner;
import org.job.hostfully.property.model.entity.Property;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class BlockServiceImplUnitTest {

    @InjectMocks
    private BlockServiceImpl blockService;

    @Mock
    private BlockRepository blockRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testSaveBlock() {
        Block blockToSave = getBlock();
        blockToSave.setId(1L);
        blockToSave.updateDate(LocalDate.of(2024, 6, 1), LocalDate.of(2024, 6, 5));
        when(blockRepository.save(any(Block.class))).thenReturn(blockToSave);
        when(blockRepository.hasActiveBlockOverlap(any(LocalDate.class), any(LocalDate.class), anyLong(), anyLong())).thenReturn(false);

        Block savedBlock = blockService.save(blockToSave);

        assertNotNull(savedBlock);
        assertEquals(1L, savedBlock.getId());
        assertEquals(LocalDate.of(2024, 6, 1), savedBlock.getStartDate());
        assertEquals(LocalDate.of(2024, 6, 5), savedBlock.getEndDate());

        verify(blockRepository, times(1)).save(blockToSave);
        verify(blockRepository, times(1)).hasActiveBlockOverlap(LocalDate.of(2024, 6, 1), LocalDate.of(2024, 6, 5), 1L, 1L);
    }

    @Test
    void testSaveBlockWithOverlap() {
        Block blockToSave = getBlock();
        blockToSave.setId(1L);
        blockToSave.updateDate(LocalDate.of(2024, 6, 1), LocalDate.of(2024, 6, 5));

        when(blockRepository.hasActiveBlockOverlap(any(LocalDate.class), any(LocalDate.class), anyLong(), anyLong())).thenReturn(true);

        Exception exception = assertThrows(OverlapException.class, () -> blockService.save(blockToSave));

        assertEquals("Date overlaps found with another active block", exception.getMessage());

        verify(blockRepository, times(0)).save(any(Block.class)); // Ensure save was not called
        verify(blockRepository, times(1)).hasActiveBlockOverlap(LocalDate.of(2024, 6, 1), LocalDate.of(2024, 6, 5), 1L, 1L);
    }

    @Test
    void testFindAllBlocks() {
        List<Block> blockList = new ArrayList<>();
        blockList.add(new Block(LocalDate.now(), LocalDate.now().plusDays(5), BlockType.MAINTENANCE, Status.ACTIVE, "Description", null));
        blockList.add(new Block(LocalDate.now(), LocalDate.now().plusDays(3), BlockType.BOOKED, Status.ACTIVE, "Description", null));

        when(blockRepository.findAll()).thenReturn(blockList);

        List<Block> result = blockService.findAll();

        assertEquals(2, result.size());
        assertEquals(BlockType.MAINTENANCE, result.get(0).getType());
        assertEquals(BlockType.BOOKED, result.get(1).getType());

        verify(blockRepository, times(1)).findAll();
    }

    @Test
    void testFindBlockById() {
        Block block = getBlock();

        when(blockRepository.findById(1L)).thenReturn(Optional.of(block));

        Block result = blockService.findById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals(BlockType.MAINTENANCE, result.getType());

        verify(blockRepository, times(1)).findById(1L);
    }

    @Test
    void testFindBlockByIdNotFound() {
        when(blockRepository.findById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(NotFoundException.class, () -> blockService.findById(1L));

        assertEquals("Block not found with ID: 1", exception.getMessage());

        verify(blockRepository, times(1)).findById(1L);
    }

    @Test
    void testDeleteBlock() {
        doNothing().when(blockRepository).deleteById(1L);

        blockService.delete(1L);

        verify(blockRepository, times(1)).deleteById(1L);
    }


    private static Block getBlock() {
        Block block = new Block(LocalDate.now(), LocalDate.now().plusDays(5), BlockType.MAINTENANCE, Status.ACTIVE, "Description", getProperty());
        block.setId(1L);
        block.setDeleted(false);
        block.setCreatedAt(LocalDateTime.now());
        block.setUpdatedAt(LocalDateTime.now());
        return block;
    }

    private static Property getProperty() {
        Property property = new Property("Property", "1st", new Owner("John Doe", "email@email.com"));
        property.setId(1L);
        property.setDeleted(false);
        property.setCreatedAt(LocalDateTime.now());
        property.setUpdatedAt(LocalDateTime.now());
        return property;
    }
}
