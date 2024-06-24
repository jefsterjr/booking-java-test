package org.job.hostfully.booking.facade;

import org.job.hostfully.block.model.entity.Block;
import org.job.hostfully.booking.model.dto.BookingDTO;
import org.job.hostfully.booking.model.dto.CreateBookingDTO;
import org.job.hostfully.booking.model.dto.UpdateBookingDTO;
import org.job.hostfully.booking.model.entity.Booking;
import org.job.hostfully.booking.model.mapper.BookingMapper;
import org.job.hostfully.booking.service.BookingService;
import org.job.hostfully.common.model.enums.BlockType;
import org.job.hostfully.common.model.enums.Status;
import org.job.hostfully.common.service.CrudService;
import org.job.hostfully.common.service.PersonService;
import org.job.hostfully.guest.model.dto.GuestDTO;
import org.job.hostfully.guest.model.entity.Guest;
import org.job.hostfully.owner.model.dto.OwnerDTO;
import org.job.hostfully.owner.model.entity.Owner;
import org.job.hostfully.property.model.dto.PropertyDTO;
import org.job.hostfully.property.model.entity.Property;
import org.job.hostfully.property.service.PropertyService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
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
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class BookingFacadeUnitTest {
    private BookingFacade bookingFacade;

    @Mock
    private PersonService<Guest> guestService;

    @Mock
    private BookingMapper bookingMapper;

    @Mock
    private BookingService bookingService;

    @Mock
    private CrudService<Block> blockService;

    @Mock
    private PropertyService propertyService;

    private AutoCloseable autoCloseable;

    @BeforeEach
    void setUp() {
        autoCloseable = MockitoAnnotations.openMocks(this);
        bookingFacade = new BookingFacade(guestService, bookingMapper, bookingService, blockService, propertyService);
    }

    @AfterEach
    void adterEach() throws Exception {
        autoCloseable.close();
    }

    @Test
    void testCreateBooking() {
        CreateBookingDTO dto = new CreateBookingDTO("John Doe", "john.doe@example.com",
                LocalDate.now(), LocalDate.now().plusDays(3), 1L, null);
        Guest guest = new Guest("John Doe", "john.doe@example.com");
        Block block = getBlock();
        Booking booking = new Booking(Status.ACTIVE, guest, block);

        when(propertyService.findById(1L)).thenReturn(block.getProperty());
        when(guestService.findByNameAndEmail("John Doe", "john.doe@example.com")).thenReturn(Optional.of(guest));
        when(blockService.save(any(Block.class))).thenReturn(block);
        when(bookingMapper.toEntity(dto, guest, block)).thenReturn(booking);
        when(bookingService.save(booking)).thenReturn(booking);
        when(bookingMapper.toDTO(booking)).thenReturn(new BookingDTO(1L, LocalDate.now(), LocalDate.now().plusDays(3), Status.ACTIVE,
                new PropertyDTO(1L, "Property", "1st", new OwnerDTO(1L, "John Doe", "email@email.com")),
                new GuestDTO(1L, "John Doe", "email@email.com")));
        BookingDTO result = bookingFacade.createBooking(dto);

        assertNotNull(result);
        assertEquals(1L, result.id());
        assertEquals(LocalDate.now(), result.startDate());
        assertEquals(LocalDate.now().plusDays(3), result.endDate());
        assertEquals(Status.ACTIVE, result.status());

        verify(propertyService, times(1)).findById(1L);
        verify(guestService, times(1)).findByNameAndEmail("John Doe", "john.doe@example.com");
        verify(blockService, times(1)).save(any(Block.class));
        verify(bookingMapper, times(1)).toEntity(dto, guest, block);
        verify(bookingService, times(1)).save(booking);
    }

    @Test
    void testUpdateBooking() {
        Long bookingId = 1L;
        UpdateBookingDTO dto = new UpdateBookingDTO(LocalDate.now().plusDays(1), LocalDate.now().plusDays(4));
        Booking booking = new Booking(Status.ACTIVE, new Guest("John", "john@email.com"), getBlock());
        booking.setId(bookingId);
        when(bookingMapper.toDTO(booking)).thenReturn(new BookingDTO(1L, LocalDate.now(), LocalDate.now().plusDays(3), Status.ACTIVE,
                new PropertyDTO(1L, "Property", "1st", new OwnerDTO(1L, "John Doe", "email@email.com")),
                new GuestDTO(1L, "John Doe", "email@email.com")));
        when(bookingService.findById(bookingId)).thenReturn(booking);
        when(bookingService.save(booking)).thenReturn(booking);
        BookingDTO result = bookingFacade.updateBooking(bookingId, dto);

        assertNotNull(result);
        assertEquals(dto.startDate(), booking.getBlock().getStartDate());
        assertEquals(dto.endDate(), booking.getBlock().getEndDate());

        verify(bookingService, times(1)).findById(bookingId);
        verify(blockService, times(1)).save(booking.getBlock());
        verify(bookingMapper, times(1)).fromDTO(dto, booking);
        verify(bookingService, times(1)).save(booking);
    }

    @Test
    void testCancelBooking() {
        Long bookingId = 1L;
        Booking booking = new Booking(Status.ACTIVE, new Guest("John", "john@email.com"), getBlock());
        booking.setId(bookingId);
        when(bookingService.save(booking)).thenReturn(booking);
        when(bookingService.findById(bookingId)).thenReturn(booking);
        bookingFacade.cancelBooking(bookingId);

        assertEquals(Status.CANCELED, booking.getStatus());
        assertEquals(Status.CANCELED, booking.getBlock().getStatus());
        verify(bookingService, times(1)).findById(bookingId);
        verify(bookingService, times(1)).save(booking);
    }

    @Test
    void testRebook() {
        Long bookingId = 1L;
        Booking booking = new Booking(Status.CANCELED, new Guest("John", "john@email.com"), getBlock());
        when(bookingMapper.toDTO(booking)).thenReturn(new BookingDTO(1L, LocalDate.now(), LocalDate.now().plusDays(3), Status.ACTIVE,
                new PropertyDTO(1L, "Property", "1st", new OwnerDTO(1L, "John Doe", "email@email.com")),
                new GuestDTO(1L, "John Doe", "email@email.com")));
        when(bookingService.findById(bookingId)).thenReturn(booking);
        when(bookingService.save(booking)).thenReturn(booking);
        BookingDTO result = bookingFacade.rebook(bookingId);

        assertNotNull(result);
        assertEquals(bookingId, result.id());
        assertEquals(Status.ACTIVE, booking.getStatus());
        assertEquals(Status.ACTIVE, booking.getBlock().getStatus());

        verify(bookingService, times(1)).findById(bookingId);
        verify(blockService, times(1)).save(booking.getBlock());
        verify(bookingService, times(1)).save(booking);
    }

    @Test
    void testRebookWithActiveBooking() {
        Long bookingId = 1L;
        Booking booking = new Booking(Status.ACTIVE, new Guest("John", "john@email.com"), getBlock());
        booking.setId(bookingId);

        when(bookingService.findById(bookingId)).thenReturn(booking);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> bookingFacade.rebook(bookingId));
        assertEquals("Original booking is not available to rebook.", exception.getMessage());
        verify(bookingService, times(1)).findById(bookingId);
        verify(blockService, times(0)).save(booking.getBlock());
        verify(bookingService, times(0)).save(booking);
    }

    @Test
    void testGetBooking() {
        Long bookingId = 1L;
        Booking booking = new Booking(Status.ACTIVE, new Guest("John", "john@email.com"), getBlock());
        booking.setId(bookingId);
        when(bookingService.findById(bookingId)).thenReturn(booking);
        when(bookingMapper.toDTO(booking)).thenReturn(new BookingDTO(bookingId, LocalDate.now(), LocalDate.now().plusDays(3), Status.ACTIVE, null, null));

        BookingDTO result = bookingFacade.getBooking(bookingId);

        assertNotNull(result);
        assertEquals(bookingId, result.id());
        assertEquals(LocalDate.now(), result.startDate());
        assertEquals(LocalDate.now().plusDays(3), result.endDate());
        assertEquals(Status.ACTIVE, result.status());

        verify(bookingService, times(1)).findById(bookingId);
        verify(bookingMapper, times(1)).toDTO(booking);
    }

    @Test
    void testGetBookings() {
        List<Booking> bookings = new ArrayList<>();
        Booking booking = new Booking(Status.ACTIVE, new Guest("John", "john@email.com"), getBlock());
        booking.setId(1L);
        bookings.add(booking);

        Booking booking2 = new Booking(Status.ACTIVE, new Guest("John", "john@email.com"), getBlock());
        booking2.setId(2L);
        bookings.add(booking2);

        when(bookingService.findAll()).thenReturn(bookings);
        when(bookingMapper.toDTO(bookings)).thenReturn(List.of(new BookingDTO(1L, LocalDate.now(), LocalDate.now().plusDays(3), Status.ACTIVE, null, null),
                new BookingDTO(2L, LocalDate.now(), LocalDate.now().plusDays(5), Status.ACTIVE, null, null)));

        List<BookingDTO> result = bookingFacade.getBookings();
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(1L, result.getFirst().id());
        assertEquals(LocalDate.now(), result.get(0).startDate());
        assertEquals(LocalDate.now().plusDays(3), result.get(0).endDate());
        assertEquals(2L, result.get(1).id());
        assertEquals(LocalDate.now(), result.get(1).startDate());
        assertEquals(LocalDate.now().plusDays(5), result.get(1).endDate());

        verify(bookingService, times(1)).findAll();
        verify(bookingMapper, times(1)).toDTO(bookings);
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
