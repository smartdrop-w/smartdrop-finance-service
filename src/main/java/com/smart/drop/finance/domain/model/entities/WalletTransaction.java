package com.smart.drop.finance.domain.model.entities;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Immutable domain entity representing wallet transactions.
 */
public record WalletTransaction(
        Integer transactionId,
        Integer walletId,
        String name,
        BigDecimal amount,
        LocalDateTime transactionDate
) {
    public WalletTransaction {
        if (walletId == null) {
            throw new IllegalArgumentException("WalletId cannot be null");
        }
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Transaction name cannot be null or blank");
        }
        if (amount == null) {
            throw new IllegalArgumentException("Amount cannot be null");
        }
    }

    public static WalletTransaction create(Integer walletId, String name, BigDecimal amount) {
        return new WalletTransaction(null, walletId, name, amount, LocalDateTime.now());
    }
}
