package com.smart.drop.planning.interfaces.rest.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Solicitud para actualizar el estado operativo de una ruta de abastecimiento")
public record UpdateRouteStatusRequest(
        @Schema(description = "Nuevo estado de la ruta (SCHEDULED, IN_PROGRESS, COMPLETED, CANCELLED)", example = "IN_PROGRESS")
        @NotBlank(message = "status is required")
        String status
) {
}
