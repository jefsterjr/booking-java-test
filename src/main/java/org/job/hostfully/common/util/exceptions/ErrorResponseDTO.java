package org.job.hostfully.common.util.exceptions;

import java.time.LocalDateTime;

public record ErrorResponseDTO(LocalDateTime timestamp,
                               int status,
                               String message) {
}
