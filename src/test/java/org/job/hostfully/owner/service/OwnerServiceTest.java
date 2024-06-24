package org.job.hostfully.owner.service;


import org.job.hostfully.common.util.exceptions.NotFoundException;
import org.job.hostfully.owner.model.entity.Owner;
import org.job.hostfully.owner.model.repository.OwnerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class OwnerServiceTest {
    @InjectMocks
    private OwnerService ownerService;

    @Mock
    private OwnerRepository ownerRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testSaveOwnerValid() {
        Owner ownerToSave = new Owner("John Doe", "john.doe@example.com");
        when(ownerRepository.save(ownerToSave)).thenReturn(ownerToSave);

        Owner savedOwner = ownerService.save(ownerToSave);

        assertNotNull(savedOwner);
        assertEquals(ownerToSave, savedOwner);

        verify(ownerRepository, times(1)).save(ownerToSave);
    }

    @Test
    void testSaveOwnerInvalid() {
        Owner ownerToSave = new Owner(null, "john.doe@example.com");

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> ownerService.save(ownerToSave));
        assertEquals("Owner must have an Email and Name set", exception.getMessage());

        verify(ownerRepository, never()).save(any());
    }

    @Test
    void testFindAllOwners() {
        List<Owner> owners = new ArrayList<>();
        owners.add(new Owner("John Doe", "john.doe@example.com"));
        owners.add(new Owner("Jane Doe", "jane.doe@example.com"));

        when(ownerRepository.findAll()).thenReturn(owners);

        List<Owner> foundOwners = ownerService.findAll();

        assertNotNull(foundOwners);
        assertEquals(2, foundOwners.size());

        verify(ownerRepository, times(1)).findAll();
    }

    @Test
    void testFindOwnerById() {
        Long ownerId = 1L;
        Owner owner = new Owner("John Doe", "john.doe@example.com");
        owner.setId(ownerId);

        when(ownerRepository.findById(ownerId)).thenReturn(Optional.of(owner));

        Owner foundOwner = ownerService.findById(ownerId);

        assertNotNull(foundOwner);
        assertEquals(ownerId, foundOwner.getId());

        verify(ownerRepository, times(1)).findById(ownerId);
    }

    @Test
    void testFindOwnerByIdNotFound() {
        Long ownerId = 1L;
        when(ownerRepository.findById(ownerId)).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class, () -> ownerService.findById(ownerId));

        assertEquals("Owner not found", exception.getMessage());

        verify(ownerRepository, times(1)).findById(ownerId);
    }

    @Test
    void testDeleteOwner() {
        Long ownerId = 1L;

        ownerService.delete(ownerId);


        verify(ownerRepository, times(1)).deleteById(ownerId);
    }

    @Test
    void testFindByNameAndEmail() {
        String name = "John Doe";
        String email = "john.doe@example.com";
        Owner owner = new Owner(name, email);

        when(ownerRepository.findTopByNameAndEmail(name, email)).thenReturn(Optional.of(owner));

        Optional<Owner> foundOwner = ownerService.findByNameAndEmail(name, email);

        assertTrue(foundOwner.isPresent());
        assertEquals(owner, foundOwner.get());

        verify(ownerRepository, times(1)).findTopByNameAndEmail(name, email);
    }

    @Test
    void testFindByNameAndEmailNotFound() {
        String name = "John Doe";
        String email = "john.doe@example.com";

        when(ownerRepository.findTopByNameAndEmail(name, email)).thenReturn(Optional.empty());

        Optional<Owner> foundOwner = ownerService.findByNameAndEmail(name, email);

        assertFalse(foundOwner.isPresent());

        verify(ownerRepository, times(1)).findTopByNameAndEmail(name, email);
    }

    @Test
    void testSaveOwnerWithNameAndEmail() {
        String name = "John Doe";
        String email = "john.doe@example.com";
        Owner expectedOwner = new Owner(name, email);

        when(ownerRepository.save(any(Owner.class))).thenReturn(expectedOwner);

        Owner savedOwner = ownerService.save(name, email);

        assertNotNull(savedOwner);
        assertEquals(expectedOwner, savedOwner);

        verify(ownerRepository, times(1)).save(any(Owner.class));
    }
}
