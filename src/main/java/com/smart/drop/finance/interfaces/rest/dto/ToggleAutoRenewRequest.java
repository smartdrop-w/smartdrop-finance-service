package com.smart.drop.finance.interfaces.rest.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Solicitud para alternar o establecer la renovación automática de una suscripción")
public record ToggleAutoRenewRequest(
        @Schema(description = "Nuevo valor de auto-renovación (si es nulo, invierte el valor actual)", example = "true")
        Boolean autoRenew
) {
}
