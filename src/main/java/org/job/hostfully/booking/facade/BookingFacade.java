package org.job.hostfully.booking.facade;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
import org.job.hostfully.common.util.exceptions.RebookException;
import org.job.hostfully.guest.model.entity.Guest;
import org.job.hostfully.property.model.entity.Property;
import org.job.hostfully.property.service.PropertyService;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
@Slf4j
public class BookingFacade {

    private final PersonService<Guest> guestService;
    private final BookingMapper bookingMapper;
    private final BookingService bookingService;
    private final CrudService<Block> blockService;
    private final PropertyService propertyService;


    @Transactional
    public BookingDTO createBooking(CreateBookingDTO dto) {
        log.info("Creating booking for propertyId: {}", dto.propertyId());
        Property property = propertyService.findById(dto.propertyId());
        Block block = createBlock(dto, property);

        Guest guest = findOrCreateGuest(dto.guestName(), dto.guestEmail());
        Booking booking = bookingMapper.toEntity(dto, guest, block);

        return bookingMapper.toDTO(bookingService.save(booking));
    }

    private Block createBlock(CreateBookingDTO dto, Property property) {
        Block block = new Block(dto.startDate(), dto.endDate(), BlockType.BOOKED, Status.ACTIVE, "Property booked", property);
        return blockService.save(block);
    }

    private Guest findOrCreateGuest(String guestName, String guestEmail) {
        Optional<Guest> optionalGuest = guestService.findByNameAndEmail(guestName, guestEmail);
        return optionalGuest.orElseGet(() -> guestService.save(guestName, guestEmail));
    }

    @Transactional
    public BookingDTO updateBooking(Long id, UpdateBookingDTO dto) {
        log.info("Updating booking with id: {}", id);
        Booking booking = bookingService.findById(id);
        updateBlockDates(booking.getBlock(), dto);
        bookingMapper.fromDTO(dto, booking);
        return bookingMapper.toDTO(bookingService.save(booking));
    }

    private void updateBlockDates(Block block, UpdateBookingDTO dto) {
        block.updateDate(dto.startDate(), dto.endDate());
        blockService.save(block);
    }

    public BookingDTO getBooking(Long id) {
        return bookingMapper.toDTO(bookingService.findById(id));
    }

    @Transactional
    public void cancelBooking(Long id) {
        log.info("Canceling booking with id: {}", id);
        Booking booking = bookingService.findById(id);
        booking.getBlock().cancel();
        booking.cancel();
        bookingService.save(booking);
    }

    @Transactional
    public BookingDTO rebook(Long id) {
        log.info("Rebooking with id: {}", id);
        Booking booking = bookingService.findById(id);
        validateRebookStatus(booking);
        booking.getBlock().activate();
        blockService.save(booking.getBlock());
        booking.activate();
        return bookingMapper.toDTO(bookingService.save(booking));
    }

    private void validateRebookStatus(Booking booking) {
        if (!Status.CANCELED.equals(booking.getStatus())) {
            throw new RebookException("Original booking is not available to rebook.");
        }
    }

    public void delete(Long id) {
        log.info("Deleting booking with id: {}", id);
        bookingService.delete(id);
    }

    public List<BookingDTO> getBookings() {

        return bookingMapper.toDTO(bookingService.findAll());
    }
}