package com.smart.drop.finance.interfaces.rest.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Request to initiate a new user subscription to a plan")
public record CreateSubscriptionRequest(
        @Schema(description = "User identifier who subscribes", example = "42")
        Integer userId,

        @Schema(description = "Plan identifier to subscribe to", example = "1")
        Integer planId,

        @Schema(description = "Whether the subscription automatically renews upon expiration", example = "true")
        Boolean autoRenew
) {
}
