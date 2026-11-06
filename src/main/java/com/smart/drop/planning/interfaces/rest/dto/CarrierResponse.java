package com.smart.drop.planning.interfaces.rest.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Ficha de datos y disponibilidad del transportista logístico")
public record CarrierResponse(
        @Schema(description = "Identificador único del transportista", example = "10")
        Integer carrierId,

        @Schema(description = "Nombre o razón social del transportista", example = "Cisternas Perú Express")
        String name,

        @Schema(description = "Teléfono de contacto para despacho", example = "+51 987654321")
        String contactPhone,

        @Schema(description = "Estado operacional del transportista (ACTIVE, INACTIVE, ON_ROUTE)", example = "ACTIVE")
        String status
) {
}
