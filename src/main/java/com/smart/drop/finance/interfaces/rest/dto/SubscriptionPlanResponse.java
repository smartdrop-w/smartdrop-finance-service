package com.smart.drop.finance.interfaces.rest.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Schema(description = "Subscription plan tier response details")
public record SubscriptionPlanResponse(
        @Schema(description = "Plan identifier", example = "1")
        Integer planId,

        @Schema(description = "Plan name", example = "SmartDrop Premium Enterprise")
        String name,

        @Schema(description = "Plan description", example = "Unlimited smart tank telemetry, automated logistics routing, and 24/7 dedicated support")
        String description,

        @Schema(description = "Billing recurring price", example = "99.90")
        BigDecimal price,

        @Schema(description = "Plan cycle duration in days", example = "30")
        Integer duration,

        @Schema(description = "Timestamp when plan was created", example = "2026-01-01T00:00:00")
        LocalDateTime createdAt
) {
}
