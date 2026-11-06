package com.smart.drop.planning.interfaces.rest.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Request to assign a carrier to a planning route")
public record AssignCarrierRequest(
        @Schema(description = "Identifier of the carrier to assign", example = "101")
        Integer carrierId
) {
}
