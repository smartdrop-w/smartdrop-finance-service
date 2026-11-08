package com.smart.drop.finance.interfaces.rest.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Schema(description = "Wallet transaction balance movement details")
public record WalletTransactionResponse(
        @Schema(description = "Transaction identifier", example = "7001")
        Integer transactionId,

        @Schema(description = "Associated wallet identifier", example = "50")
        Integer walletId,

        @Schema(description = "Transaction operation description or concept", example = "Prepaid recharge via credit card")
        String name,

        @Schema(description = "Transaction amount credited or debited", example = "150.00")
        BigDecimal amount,

        @Schema(description = "Timestamp when the transaction was executed", example = "2026-11-14T08:00:00")
        LocalDateTime transactionDate
) {
}
