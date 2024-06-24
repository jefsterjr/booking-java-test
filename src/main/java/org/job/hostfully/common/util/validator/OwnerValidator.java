package org.job.hostfully.common.util.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.job.hostfully.booking.model.dto.CreateBookingDTO;
import org.job.hostfully.common.util.annotation.OwnerValidation;
import org.job.hostfully.property.model.dto.CreatePropertyDTO;

public class OwnerValidator implements ConstraintValidator<OwnerValidation, CreatePropertyDTO> {
    @Override
    public boolean isValid(CreatePropertyDTO dto, ConstraintValidatorContext context) {
        if (dto.ownerId() != null) return true;
        return dto.ownerName() != null && dto.ownerEmail() != null;
    }
}
