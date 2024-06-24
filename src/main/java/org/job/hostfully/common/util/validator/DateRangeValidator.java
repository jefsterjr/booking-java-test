package org.job.hostfully.common.util.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.job.hostfully.common.model.interfaces.DateRange;
import org.job.hostfully.common.util.annotation.ValidDateRange;


public class DateRangeValidator implements ConstraintValidator<ValidDateRange, DateRange> {

    @Override
    public boolean isValid(DateRange dto, ConstraintValidatorContext context) {
        return dto.startDate() != null && dto.endDate() != null && (dto.startDate().isEqual(dto.endDate()) || dto.startDate().isBefore(dto.endDate()));
    }
}