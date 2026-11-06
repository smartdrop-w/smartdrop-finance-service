package com.smart.drop.planning.interfaces.rest.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Schema(description = "Detalle de ruta de despacho y logística de distribución de agua")
public record RouteResponse(
        @Schema(description = "Identificador único de la ruta", example = "1")
        Integer routeId,

        @Schema(description = "Punto de origen de despacho", example = "Planta de Tratamiento Central")
        String origin,

        @Schema(description = "Punto de destino / entrega", example = "Cervecería Artesanal Brewer")
        String destination,

        @Schema(description = "Distancia calculada en kilómetros", example = "14.50")
        BigDecimal distanceKm,

        @Schema(description = "Tiempo estimado de traslado en minutos", example = "35.00")
        BigDecimal estimatedTime,

        @Schema(description = "Identificador del transportista asignado", example = "10")
        Integer assignedCarrierId,

        @Schema(description = "Estado de la ruta (SCHEDULED, IN_PROGRESS, COMPLETED, CANCELLED)", example = "SCHEDULED")
        String status,

        @Schema(description = "Fecha de planificación de la ruta", example = "2026-11-13T23:30:00")
        LocalDateTime createdAt
) {
    public RouteResponse(Integer routeId, String origin, String destination, BigDecimal distanceKm, BigDecimal estimatedTime, Integer assignedCarrierId, LocalDateTime createdAt) {
        this(routeId, origin, destination, distanceKm, estimatedTime, assignedCarrierId, "SCHEDULED", createdAt);
    }
}
