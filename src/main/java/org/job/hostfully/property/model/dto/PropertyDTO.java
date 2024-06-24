package org.job.hostfully.property.model.dto;

import org.job.hostfully.owner.model.dto.OwnerDTO;

public record PropertyDTO(Long id,
                          String name,
                          String address,
                          OwnerDTO owner) {
}
