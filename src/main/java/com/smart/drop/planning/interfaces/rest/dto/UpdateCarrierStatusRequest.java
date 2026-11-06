package com.smart.drop.planning.interfaces.rest.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Solicitud para actualizar el estado operativo de un transportista")
public record UpdateCarrierStatusRequest(
        @Schema(description = "Nuevo estado del transportista (ACTIVE, INACTIVE, ON_ROUTE, SUSPENDED)", example = "ACTIVE")
        @NotBlank(message = "status is required")
        String status
) {
}
