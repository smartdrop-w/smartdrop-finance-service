package com.smart.drop.finance.domain.model.entities;

import java.math.BigDecimal;

/**
 * Immutable domain entity representing user wallet.
 */
public record Wallet(
        Integer walletId,
        Integer userId,
        BigDecimal balance
) {
    public Wallet {
        if (userId == null) {
            throw new IllegalArgumentException("UserId cannot be null");
        }
        if (balance == null) {
            throw new IllegalArgumentException("Balance cannot be null");
        }
    }

    public static Wallet create(Integer userId, BigDecimal balance) {
        return new Wallet(null, userId, balance);
    }
}
