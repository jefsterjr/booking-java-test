package org.job.hostfully.booking.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotNull;
import org.job.hostfully.common.model.interfaces.DateRange;
import org.job.hostfully.common.util.annotation.GuestValidation;
import org.job.hostfully.common.util.annotation.OwnerValidation;
import org.job.hostfully.common.util.annotation.ValidDateRange;

import java.time.LocalDate;
@JsonInclude(JsonInclude.Include.NON_NULL)
@ValidDateRange
@GuestValidation
public record CreateBookingDTO(String guestName,
                               String guestEmail,
                               @NotNull LocalDate startDate,
                               @NotNull LocalDate endDate,
                               @NotNull Long propertyId,
                               Long guestId
) implements DateRange {
}
