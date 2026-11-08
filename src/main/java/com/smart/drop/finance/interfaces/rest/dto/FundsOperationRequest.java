package com.smart.drop.finance.interfaces.rest.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;

@Schema(description = "Request to perform a credit or debit operation on user digital wallet")
public record FundsOperationRequest(
        @Schema(description = "Monetary amount to credit or debit", example = "150.00")
        BigDecimal amount,

        @Schema(description = "Description or concept of the funds transaction", example = "Prepaid balance recharge via credit card")
        String transactionName
) {
}
