package com.smart.drop.finance.interfaces.rest.dto;

import java.math.BigDecimal;

public record CreateSubscriptionPlanRequest(
        String name,
        String description,
        BigDecimal price,
        Integer duration
) {
}

