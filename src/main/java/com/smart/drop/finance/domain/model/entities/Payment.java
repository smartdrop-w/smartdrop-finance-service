package com.smart.drop.finance.domain.model.entities;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Immutable domain entity for payments.
 */
public record Payment(
        Integer paymentId,
        Integer subscriptionId,
        BigDecimal amount,
        String currency,
        LocalDateTime paymentDate,
        String method,
        String transactionId
) {
    public Payment {
        if (currency != null && currency.length() > 10) {
            throw new IllegalArgumentException("currency must be <= 10 characters");
        }
        if (method != null && method.length() > 50) {
            throw new IllegalArgumentException("method must be <= 50 characters");
        }
        if (transactionId != null && transactionId.length() > 100) {
            throw new IllegalArgumentException("transactionId must be <= 100 characters");
        }
    }

    public static Payment create(Integer subscriptionId, BigDecimal amount, String currency, String method, String transactionId) {
        return new Payment(null, subscriptionId, amount, currency, LocalDateTime.now(), method, transactionId);
    }
}

