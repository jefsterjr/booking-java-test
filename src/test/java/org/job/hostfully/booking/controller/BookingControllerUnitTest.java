package org.job.hostfully.booking.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.job.hostfully.booking.facade.BookingFacade;
import org.job.hostfully.booking.model.dto.BookingDTO;
import org.job.hostfully.booking.model.dto.CreateBookingDTO;
import org.job.hostfully.booking.model.dto.UpdateBookingDTO;
import org.job.hostfully.common.model.enums.Status;
import org.job.hostfully.common.util.exceptions.NotFoundException;
import org.job.hostfully.guest.model.dto.GuestDTO;
import org.job.hostfully.owner.model.dto.OwnerDTO;
import org.job.hostfully.property.model.dto.PropertyDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.MessageSource;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Collections;
import java.util.Locale;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BookingController.class)
public class BookingControllerUnitTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookingFacade facade;

    @MockBean
    private MessageSource messageSource;

    @Autowired
    private ObjectMapper objectMapper;

    private BookingDTO bookingDTO;
    private CreateBookingDTO createBookingDTO;
    private UpdateBookingDTO updateBookingDTO;

    @BeforeEach
    public void setup() {
        bookingDTO = new BookingDTO(1L, LocalDate.now(), LocalDate.now().plusDays(3), Status.ACTIVE,
                new PropertyDTO(1L, "Property", "1st",
                        new OwnerDTO(1L, "John", "email@email.com")),
                new GuestDTO(1L, "John Doe", "email@email.com"));
        createBookingDTO = new CreateBookingDTO("John Doe", "email@email.com", LocalDate.now(), LocalDate.now().plusDays(3), 1L, null);
        updateBookingDTO = new UpdateBookingDTO(LocalDate.now(), LocalDate.now().plusDays(5));

        Mockito.when(messageSource.getMessage(any(String.class), any(), any(String.class), any(Locale.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));
    }

    @Test
    public void testCreateBooking() throws Exception {
        given(facade.createBooking(any(CreateBookingDTO.class))).willReturn(bookingDTO);

        mockMvc.perform(post("/bookings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createBookingDTO)))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(jsonPath("$.id").value(bookingDTO.id()))
                .andExpect(jsonPath("$.guest.name").value(bookingDTO.guest().name()));
    }

    @Test
    public void testCreateBookingInvalidDateRange() throws Exception {
        CreateBookingDTO invalidCreateBookingDTO = new CreateBookingDTO("John Doe", "john.doe@example.com", LocalDate.now().plusDays(3), LocalDate.now(), 1L, null);

        mockMvc.perform(post("/bookings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidCreateBookingDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.type").value("about:blank"))
                .andExpect(jsonPath("$.title").value("Bad Request"))
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.detail").value("Invalid request content."))
                .andExpect(jsonPath("$.instance").value("/bookings"))
                .andExpect(jsonPath("$.validation-errors[0].timestamp").exists())
                .andExpect(jsonPath("$.validation-errors[0].status").value(400))
                .andExpect(jsonPath("$.validation-errors[0].message").value("Invalid date range"));
    }

    @Test
    public void testCreateBookingMissingFields() throws Exception {
        CreateBookingDTO invalidCreateBookingDTO = new CreateBookingDTO(null, "john.doe@example.com", LocalDate.now(), LocalDate.now().plusDays(3), 1L, null);

        mockMvc.perform(post("/bookings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidCreateBookingDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.type").value("about:blank"))
                .andExpect(jsonPath("$.title").value("Bad Request"))
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.detail").value("Invalid request content."))
                .andExpect(jsonPath("$.instance").value("/bookings"))
                .andExpect(jsonPath("$.validation-errors[0].timestamp").exists())
                .andExpect(jsonPath("$.validation-errors[0].status").value(400))
                .andExpect(jsonPath("$.validation-errors[0].message").value("Guest information must be provided"));
    }

    @Test
    public void testUpdateBooking() throws Exception {
        given(facade.updateBooking(anyLong(), any(UpdateBookingDTO.class))).willReturn(bookingDTO);

        mockMvc.perform(put("/bookings/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateBookingDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(bookingDTO.id()))
                .andExpect(jsonPath("$.guest.name").value(bookingDTO.guest().name()));
    }

    @Test
    public void testUpdateBookingInvalidDateRange() throws Exception {
        UpdateBookingDTO invalidUpdateBookingDTO = new UpdateBookingDTO(LocalDate.now().plusDays(3), LocalDate.now());

        mockMvc.perform(put("/bookings/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidUpdateBookingDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.type").value("about:blank"))
                .andExpect(jsonPath("$.title").value("Bad Request"))
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.detail").value("Invalid request content."))
                .andExpect(jsonPath("$.instance").value("/bookings/1"))
                .andExpect(jsonPath("$.validation-errors[0].timestamp").exists())
                .andExpect(jsonPath("$.validation-errors[0].status").value(400))
                .andExpect(jsonPath("$.validation-errors[0].message").value("Invalid date range"));
    }

    @Test
    public void testUpdateBookingMissingFields() throws Exception {
        UpdateBookingDTO invalidUpdateBookingDTO = new UpdateBookingDTO(null, LocalDate.now().plusDays(3));

        mockMvc.perform(put("/bookings/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidUpdateBookingDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.type").value("about:blank"))
                .andExpect(jsonPath("$.title").value("Bad Request"))
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.detail").value("Invalid request content."))
                .andExpect(jsonPath("$.instance").value("/bookings/1"))
                .andExpect(jsonPath("$.validation-errors[0].timestamp").exists())
                .andExpect(jsonPath("$.validation-errors[0].status").value(400))
                .andExpect(jsonPath("$.validation-errors[0].message").value("Invalid date range"));
    }

    @Test
    public void testGetBookingById() throws Exception {
        given(facade.getBooking(anyLong())).willReturn(bookingDTO);

        mockMvc.perform(get("/bookings/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(bookingDTO.id()))
                .andExpect(jsonPath("$.guest.name").value(bookingDTO.guest().name()));
    }

    @Test
    public void testGetBookingByIdNotFound() throws Exception {
        given(facade.getBooking(anyLong())).willThrow(new NotFoundException("Booking not found"));

        mockMvc.perform(get("/bookings/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message").value("Booking not found"));
    }

    @Test
    public void testGetBookings() throws Exception {
        given(facade.getBookings()).willReturn(Collections.singletonList(bookingDTO));

        mockMvc.perform(get("/bookings")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(bookingDTO.id()))
                .andExpect(jsonPath("$[0].guest.name").value(bookingDTO.guest().name()));
    }

    @Test
    public void testCancelBooking() throws Exception {
        mockMvc.perform(patch("/bookings/cancel/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    public void testRebook() throws Exception {
        given(facade.rebook(anyLong())).willReturn(bookingDTO);

        mockMvc.perform(patch("/bookings/rebook/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(bookingDTO.id()))
                .andExpect(jsonPath("$.guest.name").value(bookingDTO.guest().name()));
    }

    @Test
    public void testDeleteBooking() throws Exception {
        mockMvc.perform(delete("/bookings/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    public void testDeleteBookingNotFound() throws Exception {
        Mockito.doThrow(new NotFoundException("Booking not found")).when(facade).delete(anyLong());

        mockMvc.perform(delete("/bookings/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message").value("Booking not found"));
    }

}
