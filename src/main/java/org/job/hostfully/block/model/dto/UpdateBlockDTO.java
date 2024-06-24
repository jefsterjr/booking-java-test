package org.job.hostfully.block.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotNull;
import org.job.hostfully.common.model.enums.BlockType;
import org.job.hostfully.common.model.interfaces.DateRange;
import org.job.hostfully.common.util.annotation.ValidDateRange;

import java.time.LocalDate;

@JsonInclude(JsonInclude.Include.NON_NULL)
@ValidDateRange
public record UpdateBlockDTO(LocalDate startDate,
                             LocalDate endDate,
                             @NotNull BlockType type,
                             @NotNull Long propertyId,
                             String description) implements DateRange {
}
