package com.smart.drop.finance.interfaces.rest.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;

@Schema(description = "Request to create a new subscription plan tier")
public record CreateSubscriptionPlanRequest(
        @Schema(description = "Plan name", example = "SmartDrop Premium Enterprise")
        String name,

        @Schema(description = "Plan feature overview and terms", example = "Unlimited smart tank telemetry, automated logistics routing, and 24/7 dedicated support")
        String description,

        @Schema(description = "Billing recurring price", example = "99.90")
        BigDecimal price,

        @Schema(description = "Subscription billing cycle duration in days", example = "30")
        Integer duration
) {
}
