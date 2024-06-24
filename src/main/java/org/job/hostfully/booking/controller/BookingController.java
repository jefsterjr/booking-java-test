package org.job.hostfully.booking.controller;

import lombok.RequiredArgsConstructor;
import org.job.hostfully.booking.facade.BookingFacade;
import org.job.hostfully.booking.model.dto.BookingDTO;
import org.job.hostfully.booking.model.dto.CreateBookingDTO;
import org.job.hostfully.booking.model.dto.UpdateBookingDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
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
@RequestMapping("/bookings")
@RequiredArgsConstructor
public class BookingController {
    private final BookingFacade facade;

    @PostMapping
    public ResponseEntity<BookingDTO> createBooking(@Validated @RequestBody CreateBookingDTO dto) {
        BookingDTO result = facade.createBooking(dto);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(result.id())
                .toUri();
        return ResponseEntity.created(location).body(result);
    }

    @PutMapping("/{id}")
    public ResponseEntity<BookingDTO> updateBooking(@PathVariable Long id,
                                                    @Validated @RequestBody UpdateBookingDTO dto) {
        BookingDTO result = facade.updateBooking(id, dto);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookingDTO> getBooking(@PathVariable Long id) {
        BookingDTO result = facade.getBooking(id);
        return ResponseEntity.ok(result);
    }

    @GetMapping
    public ResponseEntity<List<BookingDTO>> getBookings() {
        List<BookingDTO> result = facade.getBookings();
        return ResponseEntity.ok(result);
    }

    @PatchMapping("/cancel/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void cancelBooking(@PathVariable Long id) {
        facade.cancelBooking(id);
    }


    @PatchMapping("/rebook/{id}")
    public ResponseEntity<BookingDTO> rebook(@PathVariable Long id) {
        BookingDTO result = facade.rebook(id);
        return ResponseEntity.ok(result);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteBooking(@PathVariable Long id) {
        facade.delete(id);
    }

}
