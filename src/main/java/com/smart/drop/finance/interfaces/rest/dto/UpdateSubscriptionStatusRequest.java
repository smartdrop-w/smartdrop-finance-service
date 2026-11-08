package com.smart.drop.finance.interfaces.rest.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Request to update subscription lifecycle status")
public record UpdateSubscriptionStatusRequest(
        @Schema(description = "New subscription status (ACTIVE, SUSPENDED, CANCELLED)", example = "ACTIVE")
        String status
) {
}
