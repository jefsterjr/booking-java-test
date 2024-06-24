package org.job.hostfully.booking.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.job.hostfully.common.model.interfaces.DateRange;
import org.job.hostfully.common.util.annotation.ValidDateRange;

import java.time.LocalDate;
@JsonInclude(JsonInclude.Include.NON_NULL)
@ValidDateRange
public record UpdateBookingDTO(LocalDate startDate,
                               LocalDate endDate) implements DateRange {
}
