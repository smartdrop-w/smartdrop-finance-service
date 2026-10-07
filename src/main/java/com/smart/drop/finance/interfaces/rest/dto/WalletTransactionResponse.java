package com.smart.drop.finance.interfaces.rest.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record WalletTransactionResponse(
        Integer transactionId,
        Integer walletId,
        String name,
        BigDecimal amount,
        LocalDateTime transactionDate
) {
}
