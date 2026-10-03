package com.smart.drop.finance.domain.model.entities;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Immutable domain entity for invoices.
 */
public record Invoice(
        Integer invoiceId,
        Integer subscriptionId,
        LocalDateTime issuedAt,
        BigDecimal total,
        String currency
) {
    public Invoice {
        if (currency != null && currency.length() > 10) {
            throw new IllegalArgumentException("currency must be <= 10 characters");
        }
    }

    public static Invoice create(Integer subscriptionId, BigDecimal total, String currency) {
        return new Invoice(null, subscriptionId, LocalDateTime.now(), total, currency);
    }
}

