package org.job.hostfully.property.service;

import org.job.hostfully.common.util.exceptions.NotFoundException;
import org.job.hostfully.owner.model.entity.Owner;
import org.job.hostfully.property.model.entity.Property;
import org.job.hostfully.property.model.repository.PropertyRepository;
import org.job.hostfully.property.service.impl.PropertyServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class PropertyServiceUnitTest {
    @InjectMocks
    private PropertyServiceImpl propertyService;

    @Mock
    private PropertyRepository propertyRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testSaveProperty() {
        Property propertyToSave = new Property("Property 1", "Address 1", new Owner("John", "email@email.com"));
        when(propertyRepository.save(propertyToSave)).thenReturn(propertyToSave);

        Property savedProperty = propertyService.save(propertyToSave);

        assertNotNull(savedProperty);
        assertEquals(propertyToSave, savedProperty);

        verify(propertyRepository, times(1)).save(propertyToSave);
    }

    @Test
    void testFindAllProperties() {
        List<Property> properties = new ArrayList<>();
        properties.add(new Property("Property 1", "Address 1", new Owner("John", "email@email.com")));
        properties.add(new Property("Property 2", "Address 2", new Owner("John", "email@email.com")));

        when(propertyRepository.findAll()).thenReturn(properties);

        List<Property> foundProperties = propertyService.findAll();
        assertNotNull(foundProperties);
        assertEquals(2, foundProperties.size());

        verify(propertyRepository, times(1)).findAll();
    }

    @Test
    void testFindPropertyById() {
        Long propertyId = 1L;
        Property property = new Property("Property 1", "Address 1", new Owner("John", "email@email.com"));
        property.setId(propertyId);

        when(propertyRepository.findById(propertyId)).thenReturn(Optional.of(property));

        Property foundProperty = propertyService.findById(propertyId);

        assertNotNull(foundProperty);
        assertEquals(propertyId, foundProperty.getId());
        verify(propertyRepository, times(1)).findById(propertyId);
    }

    @Test
    void testFindPropertyByIdNotFound() {
        Long propertyId = 1L;
        when(propertyRepository.findById(propertyId)).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class, () -> propertyService.findById(propertyId));

        assertEquals("Property not found", exception.getMessage());

        verify(propertyRepository, times(1)).findById(propertyId);
    }

    @Test
    void testDeleteProperty() {
        Long propertyId = 1L;

        propertyService.delete(propertyId);

        verify(propertyRepository, times(1)).deleteById(propertyId);
    }
}
