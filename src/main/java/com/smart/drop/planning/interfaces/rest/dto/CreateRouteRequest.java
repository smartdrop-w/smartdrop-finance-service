package com.smart.drop.planning.interfaces.rest.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Request to create a new water distribution route")
public record CreateRouteRequest(
        @Schema(description = "Starting location address or coordinates", example = "Water Treatment Plant Central, Sector A")
        String origin,

        @Schema(description = "Delivery destination address or zone", example = "Sector 4 Industrial Tank Cluster, Ave. Las Palmeras 120")
        String destination,

        @Schema(description = "Identifier of assigned carrier or null if unassigned", example = "101")
        Integer assignedCarrierId
) {
}
