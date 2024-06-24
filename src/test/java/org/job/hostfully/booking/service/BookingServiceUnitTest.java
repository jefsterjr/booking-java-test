package org.job.hostfully.booking.service;


import org.job.hostfully.booking.model.entity.Booking;
import org.job.hostfully.booking.model.repository.BookingRepository;
import org.job.hostfully.booking.service.impl.BookingServiceImpl;
import org.job.hostfully.common.model.enums.Status;
import org.job.hostfully.common.util.exceptions.NotFoundException;
import org.job.hostfully.guest.model.entity.Guest;
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

public class BookingServiceUnitTest {
    @InjectMocks
    private BookingServiceImpl bookingService;

    @Mock
    private BookingRepository bookingRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testSaveBooking() {
        Booking bookingToSave = new Booking(Status.ACTIVE, new Guest("John", "john@email.com"), null);
        when(bookingRepository.save(bookingToSave)).thenReturn(bookingToSave);

        Booking savedBooking = bookingService.save(bookingToSave);

        assertNotNull(savedBooking);
        assertEquals(bookingToSave, savedBooking);

        verify(bookingRepository, times(1)).save(bookingToSave);
    }

    @Test
    void testFindAllBookings() {
        List<Booking> bookings = new ArrayList<>();
        bookings.add(new Booking(Status.ACTIVE, new Guest("John", "john@email.com"), null));
        bookings.add(new Booking(Status.CANCELED, new Guest("John", "john@email.com"), null));

        when(bookingRepository.findAll()).thenReturn(bookings);

        List<Booking> foundBookings = bookingService.findAll();

        assertNotNull(foundBookings);
        assertEquals(2, foundBookings.size());

        verify(bookingRepository, times(1)).findAll();
    }

    @Test
    void testFindBookingById() {
        Long bookingId = 1L;
        Booking booking = new Booking(Status.ACTIVE, null, null);
        booking.setId(bookingId);

        when(bookingRepository.findById(bookingId)).thenReturn(Optional.of(booking));
        Booking foundBooking = bookingService.findById(bookingId);

        assertNotNull(foundBooking);
        assertEquals(bookingId, foundBooking.getId());

        verify(bookingRepository, times(1)).findById(bookingId);
    }

    @Test
    void testFindBookingByIdNotFound() {
        Long bookingId = 1L;
        when(bookingRepository.findById(bookingId)).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class, () -> bookingService.findById(bookingId));

        assertEquals("Booking not found with ID: " + bookingId, exception.getMessage());

        verify(bookingRepository, times(1)).findById(bookingId);
    }

    @Test
    void testDeleteBooking() {
        Long bookingId = 1L;

        bookingService.delete(bookingId);

        verify(bookingRepository, times(1)).deleteById(bookingId);
    }
}
