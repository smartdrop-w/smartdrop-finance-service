package com.smart.drop.planning.interfaces.rest.dto;

public record CreateRouteRequest(
        String origin,
        String destination,
        Integer assignedCarrierId
) {
}

