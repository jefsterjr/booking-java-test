package org.job.hostfully.block.controller;

import lombok.RequiredArgsConstructor;
import org.job.hostfully.block.facade.BlockFacade;
import org.job.hostfully.block.model.dto.BlockDTO;
import org.job.hostfully.block.model.dto.CreateBlockDTO;
import org.job.hostfully.block.model.dto.UpdateBlockDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/blocks")
@RequiredArgsConstructor
public class BlockController {
    private final BlockFacade facade;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<BlockDTO> createBlock(@Validated @RequestBody CreateBlockDTO dto) {
        BlockDTO block = facade.create(dto);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(block.id()).toUri();
        return ResponseEntity.created(location).body(block);
    }

    @PutMapping("/{blockId}")
    public ResponseEntity<BlockDTO> updateBlock(@PathVariable Long blockId, @Validated @RequestBody UpdateBlockDTO dto) {
        BlockDTO block = facade.update(blockId, dto);
        return ResponseEntity.ok(block);
    }

    @GetMapping("/{blockId}")
    public ResponseEntity<BlockDTO> getBlockById(@PathVariable Long blockId) {
        BlockDTO block = facade.findById(blockId);
        return ResponseEntity.ok(block);
    }

    @GetMapping
    public ResponseEntity<List<BlockDTO>> getBlocks() {
        List<BlockDTO> blocks = facade.findAll();
        return ResponseEntity.ok(blocks);
    }

    @DeleteMapping("/{blockId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteBlock(@PathVariable Long blockId) {
        facade.delete(blockId);
    }
}
