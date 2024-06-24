package org.job.hostfully.property.model.dto;

import jakarta.validation.constraints.NotNull;
import org.job.hostfully.common.util.annotation.OwnerValidation;

@OwnerValidation
public record CreatePropertyDTO(@NotNull String name,
                                @NotNull String address,
                                Long ownerId,
                                String ownerName,
                                String ownerEmail
) {
}
