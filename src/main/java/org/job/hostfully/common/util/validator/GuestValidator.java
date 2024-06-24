package org.job.hostfully.common.util.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.job.hostfully.booking.model.dto.CreateBookingDTO;
import org.job.hostfully.common.util.annotation.GuestValidation;

public class GuestValidator implements ConstraintValidator<GuestValidation, CreateBookingDTO> {
    @Override
    public boolean isValid(CreateBookingDTO dto, ConstraintValidatorContext context) {
        if (dto.guestId() != null) return true;
        return dto.guestName() != null && dto.guestEmail() != null;
    }
}
