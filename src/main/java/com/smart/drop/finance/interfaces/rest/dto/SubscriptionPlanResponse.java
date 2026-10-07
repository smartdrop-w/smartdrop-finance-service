package com.smart.drop.finance.interfaces.rest.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record SubscriptionPlanResponse(
        Integer planId,
        String name,
        String description,
        BigDecimal price,
        Integer duration,
        LocalDateTime createdAt
) {
}

