package com.smart.drop.finance.interfaces.rest.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;

@Schema(description = "User digital wallet details and current balance")
public record WalletResponse(
        @Schema(description = "Wallet identifier", example = "50")
        Integer walletId,

        @Schema(description = "Owner user identifier", example = "42")
        Integer userId,

        @Schema(description = "Available wallet balance", example = "350.50")
        BigDecimal balance
) {
}
