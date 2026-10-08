package com.smart.drop.finance.domain.model.entities;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Immutable domain entity for subscription plans.
 */
public record SubscriptionPlan(
        Integer planId,
        String name,
        String description,
        BigDecimal price,
        Integer duration,
        LocalDateTime createdAt
) {
    public SubscriptionPlan {
        if (name != null && name.length() > 50) {
            throw new IllegalArgumentException("name must be <= 50 characters");
        }
        if (duration != null && duration < 0) {
            throw new IllegalArgumentException("duration must be >= 0");
        }
    }

    public static SubscriptionPlan create(String name, String description, BigDecimal price, Integer duration) {
        return new SubscriptionPlan(null, name, description, price, duration, LocalDateTime.now());
    }
}

