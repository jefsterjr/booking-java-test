package org.job.hostfully.booking.service.impl;

import lombok.RequiredArgsConstructor;
import org.job.hostfully.booking.model.entity.Booking;
import org.job.hostfully.booking.model.repository.BookingRepository;
import org.job.hostfully.booking.service.BookingService;
import org.job.hostfully.common.util.exceptions.NotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;

    @Override
    @Transactional
    public Booking save(Booking booking) {
        return bookingRepository.save(booking);
    }

    @Override
    public List<Booking> findAll() {
        return bookingRepository.findAll();
    }

    @Override
    public Booking findById(Long id) {
        return bookingRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Booking not found with ID: " + id));
    }

    @Override
    @Transactional
    public void delete(Long id) {
        bookingRepository.deleteById(id);
    }
}
