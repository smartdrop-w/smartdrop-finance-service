package com.smart.drop.planning.interfaces.rest.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record RouteResponse(
        Integer routeId,
        String origin,
        String destination,
        BigDecimal distanceKm,
        BigDecimal estimatedTime,
        Integer assignedCarrierId,
        LocalDateTime createdAt
) {
}

