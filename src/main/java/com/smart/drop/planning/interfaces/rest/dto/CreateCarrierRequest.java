package com.smart.drop.planning.interfaces.rest.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Request to register a new logistics carrier")
public record CreateCarrierRequest(
        @Schema(description = "Legal or commercial name of the carrier", example = "TransAqua Logistics S.A.C.")
        String name,

        @Schema(description = "Contact phone number with international format", example = "+51987654321")
        String contactPhone
) {
}
