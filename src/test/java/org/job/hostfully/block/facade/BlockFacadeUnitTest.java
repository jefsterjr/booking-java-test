package org.job.hostfully.block.facade;

import org.job.hostfully.block.model.dto.BlockDTO;
import org.job.hostfully.block.model.dto.CreateBlockDTO;
import org.job.hostfully.block.model.dto.UpdateBlockDTO;
import org.job.hostfully.block.model.entity.Block;
import org.job.hostfully.block.model.mapper.BlockMapper;
import org.job.hostfully.common.model.enums.BlockType;
import org.job.hostfully.common.model.enums.Status;
import org.job.hostfully.common.service.CrudService;
import org.job.hostfully.owner.model.entity.Owner;
import org.job.hostfully.property.model.entity.Property;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class BlockFacadeUnitTest {

    @Mock
    private CrudService<Property> propertyService;
    @Mock
    private BlockMapper blockMapper;
    @Mock
    private CrudService<Block> blockService;

    @InjectMocks
    private BlockFacade blockFacade;

    @BeforeEach
    void setUp() {
        blockFacade = new BlockFacade(propertyService, blockMapper, blockService);
    }

    @Test
    void testCreateBlock() {
        CreateBlockDTO createBlockDTO = new CreateBlockDTO(LocalDate.now(), LocalDate.now().plusDays(5),
                BlockType.MAINTENANCE, 1L, "Description");


        Block block = getBlock();
        Property property = block.getProperty();
        when(propertyService.findById(eq(1L))).thenReturn(property);
        when(blockMapper.toEntity(createBlockDTO, property)).thenReturn(block);
        when(blockService.save(any())).thenReturn(block);
        when(blockMapper.toDTO(eq(block))).thenReturn(new BlockDTO(1L, LocalDate.now(), LocalDate.now().plusDays(5),
                BlockType.MAINTENANCE, Status.ACTIVE, "Description", 1L));

        // When
        BlockDTO result = blockFacade.create(createBlockDTO);

        assertEquals(1L, result.id());
        assertEquals(LocalDate.now(), result.startDate());
        assertEquals(LocalDate.now().plusDays(5), result.endDate());
        assertEquals(BlockType.MAINTENANCE, result.type());
        assertEquals(Status.ACTIVE, result.status());
        assertEquals("Description", result.description());
        assertEquals(1L, result.propertyId());

        // Verify interactions
        verify(propertyService, times(1)).findById(1L);
        verify(blockMapper, times(1)).toEntity(createBlockDTO, property);
        verify(blockService, times(1)).save(block);
        verify(blockMapper, times(1)).toDTO(block);
    }


    @Test
    void testUpdateBlock() {
        Long blockId = 1L;
        UpdateBlockDTO updateBlockDTO = new UpdateBlockDTO(LocalDate.now(), LocalDate.now().plusDays(5),
                BlockType.MAINTENANCE, 1L, "Description");

        Block existingBlock =getBlock();
        existingBlock.setId(blockId);

        given(blockService.findById(blockId)).willReturn(existingBlock);

        blockFacade.update(blockId, updateBlockDTO);

        ArgumentCaptor<Block> blockArgumentCaptor = ArgumentCaptor.forClass(Block.class);
        verify(blockService, times(1)).findById(blockId);
        verify(blockMapper, times(1)).updateEntityFromDto(updateBlockDTO, existingBlock);
        verify(blockService, times(1)).save(blockArgumentCaptor.capture());

        Block updatedBlock = blockArgumentCaptor.getValue();
        assertEquals(blockId, updatedBlock.getId());
        assertEquals(LocalDate.now(), updatedBlock.getStartDate());
        assertEquals(LocalDate.now().plusDays(5), updatedBlock.getEndDate());
        assertEquals(BlockType.MAINTENANCE, updatedBlock.getType());
        assertEquals("Description", updatedBlock.getDescription());
    }

    @Test
    void testFindById() {
        Long blockId = 1L;
        Block block = getBlock();
        block.setId(blockId);

        given(blockService.findById(blockId)).willReturn(block);
        given(blockMapper.toDTO(block)).willReturn(new BlockDTO(blockId, LocalDate.now(), LocalDate.now().plusDays(5),
                BlockType.MAINTENANCE, Status.ACTIVE, "Description", 1L));

        BlockDTO result = blockFacade.findById(blockId);

        assertEquals(1L, result.id());
        assertEquals(LocalDate.now(), result.startDate());
        assertEquals(LocalDate.now().plusDays(5), result.endDate());
        assertEquals(BlockType.MAINTENANCE, result.type());
        assertEquals(Status.ACTIVE, result.status());
        assertEquals("Description", result.description());
        assertEquals(1L, result.propertyId());

        verify(blockService, times(1)).findById(blockId);
        verify(blockMapper, times(1)).toDTO(block);
    }

    @Test
    void testDelete() {
        Long blockId = 1L;
        blockFacade.delete(blockId);
        verify(blockService, times(1)).delete(blockId);
    }

    @Test
    void testFindAll() {
        List<Block> blocks = Collections.singletonList(getBlock());
        given(blockService.findAll()).willReturn(blocks);
        given(blockMapper.toDTO(blocks)).willReturn(Collections.singletonList(
                new BlockDTO(1L, LocalDate.now(), LocalDate.now().plusDays(5), BlockType.MAINTENANCE,
                        Status.ACTIVE, "Description", 1L)));
        List<BlockDTO> result = blockFacade.findAll();

        assertEquals(1, result.size());
        BlockDTO blockDTO = result.getFirst();
        assertEquals(1L, blockDTO.id());
        assertEquals(LocalDate.now(), blockDTO.startDate());
        assertEquals(LocalDate.now().plusDays(5), blockDTO.endDate());
        assertEquals(BlockType.MAINTENANCE, blockDTO.type());
        assertEquals(Status.ACTIVE, blockDTO.status());
        assertEquals("Description", blockDTO.description());
        assertEquals(1L, blockDTO.propertyId());

        verify(blockService, times(1)).findAll();
        verify(blockMapper, times(1)).toDTO(blocks);
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
