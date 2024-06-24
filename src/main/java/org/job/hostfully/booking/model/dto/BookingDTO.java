package org.job.hostfully.booking.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.job.hostfully.common.model.enums.Status;
import org.job.hostfully.guest.model.dto.GuestDTO;
import org.job.hostfully.property.model.dto.PropertyDTO;

import java.time.LocalDate;
@JsonInclude(JsonInclude.Include.NON_NULL)
public record BookingDTO(Long id,
                         LocalDate startDate,
                         LocalDate endDate,
                         Status status,
                         PropertyDTO property,
                         GuestDTO guest) {
}
