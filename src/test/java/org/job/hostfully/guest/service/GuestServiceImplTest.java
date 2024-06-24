package org.job.hostfully.guest.service;

import org.job.hostfully.common.util.exceptions.NotFoundException;
import org.job.hostfully.guest.model.entity.Guest;
import org.job.hostfully.guest.model.repository.GuestRepository;
import org.job.hostfully.guest.service.GuestServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class GuestServiceImplTest {
    @InjectMocks
    private GuestServiceImpl guestService;

    @Mock
    private GuestRepository guestRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testSaveGuest() {
        Guest guestToSave = new Guest("John Doe", "john.doe@example.com");
        when(guestRepository.save(guestToSave)).thenReturn(guestToSave);

        Guest savedGuest = guestService.save(guestToSave);

        assertNotNull(savedGuest);
        assertEquals(guestToSave, savedGuest);

        verify(guestRepository, times(1)).save(guestToSave);
    }

    @Test
    void testFindAllGuests() {
        // Given
        List<Guest> guests = new ArrayList<>();
        guests.add(new Guest("John Doe", "john.doe@example.com"));
        guests.add(new Guest("Jane Smith", "jane.smith@example.com"));

        when(guestRepository.findAll()).thenReturn(guests);

        // When
        List<Guest> foundGuests = guestService.findAll();

        // Then
        assertNotNull(foundGuests);
        assertEquals(2, foundGuests.size());

        // Verify interaction with repository
        verify(guestRepository, times(1)).findAll();
    }

    @Test
    void testFindGuestById() {
        // Given
        Long guestId = 1L;
        Guest guest = new Guest("John Doe", "john.doe@example.com");
        guest.setId(guestId);

        when(guestRepository.findById(guestId)).thenReturn(Optional.of(guest));

        // When
        Guest foundGuest = guestService.findById(guestId);

        // Then
        assertNotNull(foundGuest);
        assertEquals(guestId, foundGuest.getId());

        // Verify interaction with repository
        verify(guestRepository, times(1)).findById(guestId);
    }

    @Test
    void testFindGuestByIdNotFound() {
        Long guestId = 1L;
        when(guestRepository.findById(guestId)).thenReturn(Optional.empty());

        NotFoundException exception = Assertions.assertThrows(NotFoundException.class, () -> guestService.findById(guestId));

        assertEquals("Guest not found.", exception.getMessage());
        verify(guestRepository, times(1)).findById(guestId);
    }

    @Test
    void testFindGuestByNameAndEmail() {
        String name = "John Doe";
        String email = "john.doe@example.com";
        Guest guest = new Guest(name, email);

        when(guestRepository.findTopByNameAndEmail(name, email)).thenReturn(Optional.of(guest));

        Optional<Guest> foundGuest = guestService.findByNameAndEmail(name, email);

        assertTrue(foundGuest.isPresent());
        assertEquals(guest, foundGuest.get());

        verify(guestRepository, times(1)).findTopByNameAndEmail(name, email);
    }

    @Test
    void testFindGuestByNameAndEmailNotFound() {
        String name = "John Doe";
        String email = "john.doe@example.com";

        when(guestRepository.findTopByNameAndEmail(name, email)).thenReturn(Optional.empty());

        Optional<Guest> foundGuest = guestService.findByNameAndEmail(name, email);

        assertFalse(foundGuest.isPresent());

        verify(guestRepository, times(1)).findTopByNameAndEmail(name, email);
    }

    @Test
    void testSaveGuestWithNameAndEmail() {
        String name = "John Doe";
        String email = "john.doe@example.com";
        Guest guestToSave = new Guest(name, email);

        when(guestRepository.save(ArgumentMatchers.any())).thenReturn(guestToSave);

        Guest savedGuest = guestService.save(name, email);

        assertNotNull(savedGuest);
        assertEquals(name, savedGuest.getName());
        assertEquals(email, savedGuest.getEmail());

        verify(guestRepository, times(1)).save(ArgumentMatchers.any(Guest.class));
    }

    @Test
    void testDeleteGuest() {
        Long guestId = 1L;

        guestService.delete(guestId);

        verify(guestRepository, times(1)).deleteById(guestId);
    }
}
