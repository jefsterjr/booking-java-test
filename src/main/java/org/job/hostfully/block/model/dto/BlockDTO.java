package org.job.hostfully.block.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.job.hostfully.common.model.enums.BlockType;
import org.job.hostfully.common.model.enums.Status;

import java.time.LocalDate;
@JsonInclude(JsonInclude.Include.NON_NULL)
public record BlockDTO(Long id,
                       LocalDate startDate,
                       LocalDate endDate,
                       BlockType type,
                       Status status,
                       String description,
                       Long propertyId) {
}
