package org.job.hostfully.property.controller;

import lombok.RequiredArgsConstructor;
import org.job.hostfully.property.facade.PropertyFacade;
import org.job.hostfully.property.model.dto.CreatePropertyDTO;
import org.job.hostfully.property.model.dto.PropertyDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/properties")
@RequiredArgsConstructor
public class PropertyController {
    private final PropertyFacade propertyFacade;

    @GetMapping("/{id}")
    public PropertyDTO getProperty(@PathVariable Long id) {
        return propertyFacade.findById(id);
    }


    @PostMapping
    public ResponseEntity<PropertyDTO> createProperty(@RequestBody @Validated CreatePropertyDTO dto) {
        PropertyDTO propertyDTO = propertyFacade.create(dto);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(propertyDTO.id())
                .toUri();
        return ResponseEntity.created(location)
                .build();
    }

    @GetMapping
    public List<PropertyDTO> findAll() {
        return propertyFacade.findAll();
    }
}
