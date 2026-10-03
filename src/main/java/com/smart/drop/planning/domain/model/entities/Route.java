package com.smart.drop.planning.domain.model.entities;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Immutable domain entity for planned service routes.
 */
public record Route(
        Integer routeId,
        String origin,
        String destination,
        BigDecimal distanceKm,
        BigDecimal estimatedTime,
        Integer assignedCarrierId,
        LocalDateTime createdAt
) {
    public Route {
        if (origin == null || origin.isBlank()) {
            throw new IllegalArgumentException("origin is required");
        }
        if (destination == null || destination.isBlank()) {
            throw new IllegalArgumentException("destination is required");
        }
        if (origin.length() > 255) {
            throw new IllegalArgumentException("origin must be <= 255 characters");
        }
        if (destination.length() > 255) {
            throw new IllegalArgumentException("destination must be <= 255 characters");
        }
        if (distanceKm == null || distanceKm.signum() < 0) {
            throw new IllegalArgumentException("distanceKm must be >= 0");
        }
        if (estimatedTime == null || estimatedTime.signum() < 0) {
            throw new IllegalArgumentException("estimatedTime must be >= 0");
        }
    }

    public static Route create(String origin,
                               String destination,
                               BigDecimal distanceKm,
                               BigDecimal estimatedTime,
                               Integer assignedCarrierId) {
        return new Route(null, origin, destination, distanceKm, estimatedTime, assignedCarrierId, LocalDateTime.now());
    }

    public Route assignCarrier(Integer carrierId) {
        return new Route(routeId, origin, destination, distanceKm, estimatedTime, carrierId, createdAt);
    }
}

