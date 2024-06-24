package org.job.hostfully.block.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.job.hostfully.block.facade.BlockFacade;
import org.job.hostfully.block.model.dto.BlockDTO;
import org.job.hostfully.block.model.dto.CreateBlockDTO;
import org.job.hostfully.block.model.dto.UpdateBlockDTO;
import org.job.hostfully.common.model.enums.BlockType;
import org.job.hostfully.common.model.enums.Status;
import org.job.hostfully.common.util.exceptions.NotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.MessageSource;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Collections;
import java.util.Locale;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BlockController.class)
public class BlockControllerUnitTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BlockFacade facade;

    @MockBean
    private MessageSource messageSource;

    @Autowired
    private ObjectMapper objectMapper;

    private BlockDTO blockDTO;
    private CreateBlockDTO createBlockDTO;
    private UpdateBlockDTO updateBlockDTO;

    @BeforeEach
    public void setup() {
        blockDTO = new BlockDTO(1L, LocalDate.now(), LocalDate.now().plusDays(5), BlockType.MAINTENANCE, Status.ACTIVE, "Description", 1L);
        createBlockDTO = new CreateBlockDTO(LocalDate.now(), LocalDate.now().plusDays(5), BlockType.PRIVATE_USE, 1L, "PRIVATE_USE");
        updateBlockDTO = new UpdateBlockDTO(LocalDate.now(), LocalDate.now().plusDays(5), BlockType.MAINTENANCE, 1L, "Description");

        Mockito.when(messageSource.getMessage(any(String.class), any(), any(String.class), any(Locale.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));
    }

    @Test
    public void testCreateBlock() throws Exception {
        given(facade.create(any(CreateBlockDTO.class))).willReturn(blockDTO);

        mockMvc.perform(post("/blocks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createBlockDTO)))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(jsonPath("$.id").value(blockDTO.id()))
                .andExpect(jsonPath("$.description").value(blockDTO.description()));
    }

    @Test
    public void testCreateBlockInvalidDateRange() throws Exception {
        CreateBlockDTO invalidCreateBlockDTO = new CreateBlockDTO(LocalDate.now().plusDays(5), LocalDate.now(), BlockType.MAINTENANCE, 1L, "MAINTENANCE");

        mockMvc.perform(post("/blocks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidCreateBlockDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.title").value("Bad Request"))
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.detail").value("Invalid request content."))
                .andExpect(jsonPath("$.instance").value("/blocks"))
                .andExpect(jsonPath("$.validation-errors[0].timestamp").exists())
                .andExpect(jsonPath("$.validation-errors[0].status").value(400))
                .andExpect(jsonPath("$.validation-errors[0].message").value("Invalid date range"));
    }

    @Test
    public void testCreateBlockMissingFields() throws Exception {
        CreateBlockDTO invalidCreateBlockDTO = new CreateBlockDTO(null, LocalDate.now().plusDays(5), BlockType.MAINTENANCE, null, "MAINTENANCE");

        mockMvc.perform(post("/blocks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidCreateBlockDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.title").value("Bad Request"))
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.detail").value("Invalid request content."))
                .andExpect(jsonPath("$.instance").value("/blocks"))
                .andExpect(jsonPath("$.validation-errors[0].timestamp").exists())
                .andExpect(jsonPath("$.validation-errors[0].status").value(400));
    }

    @Test
    public void testUpdateBlock() throws Exception {
        given(facade.update(anyLong(), any(UpdateBlockDTO.class))).willReturn(blockDTO);

        mockMvc.perform(put("/blocks/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateBlockDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(blockDTO.id()))
                .andExpect(jsonPath("$.description").value(blockDTO.description()));
    }

    @Test
    public void testUpdateBlockInvalidDateRange() throws Exception {
        UpdateBlockDTO invalidUpdateBlockDTO = new UpdateBlockDTO(LocalDate.now().plusDays(5), LocalDate.now(), BlockType.PRIVATE_USE, 1L, "PRIVATE_USE");

        mockMvc.perform(put("/blocks/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidUpdateBlockDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.title").value("Bad Request"))
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.detail").value("Invalid request content."))
                .andExpect(jsonPath("$.instance").value("/blocks/1"))
                .andExpect(jsonPath("$.validation-errors[0].timestamp").exists())
                .andExpect(jsonPath("$.validation-errors[0].status").value(400))
                .andExpect(jsonPath("$.validation-errors[0].message").value("Invalid date range"));
    }

    @Test
    public void testUpdateBlockMissingFields() throws Exception {
        UpdateBlockDTO invalidUpdateBlockDTO = new UpdateBlockDTO(null, LocalDate.now().plusDays(5), BlockType.PRIVATE_USE, null, "PRIVATE_USE");

        mockMvc.perform(put("/blocks/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidUpdateBlockDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.title").value("Bad Request"))
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.detail").value("Invalid request content."))
                .andExpect(jsonPath("$.instance").value("/blocks/1"))
                .andExpect(jsonPath("$.validation-errors[0].timestamp").exists())
                .andExpect(jsonPath("$.validation-errors[0].status").value(400));
    }

    @Test
    public void testGetBlockById() throws Exception {
        given(facade.findById(anyLong())).willReturn(blockDTO);

        mockMvc.perform(get("/blocks/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(blockDTO.id()))
                .andExpect(jsonPath("$.description").value(blockDTO.description()));
    }

    @Test
    public void testGetBlockByIdNotFound() throws Exception {
        given(facade.findById(anyLong())).willThrow(new NotFoundException("Block not found"));

        mockMvc.perform(get("/blocks/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message").value("Block not found"));
    }

    @Test
    public void testGetBlocks() throws Exception {
        given(facade.findAll()).willReturn(Collections.singletonList(blockDTO));

        mockMvc.perform(get("/blocks")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(blockDTO.id()))
                .andExpect(jsonPath("$[0].description").value(blockDTO.description()));
    }

    @Test
    public void testDeleteBlock() throws Exception {
        mockMvc.perform(delete("/blocks/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    public void testDeleteBlockNotFound() throws Exception {
        Mockito.doThrow(new NotFoundException("Block not found")).when(facade).delete(anyLong());

        mockMvc.perform(delete("/blocks/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message").value("Block not found"));
    }

}
