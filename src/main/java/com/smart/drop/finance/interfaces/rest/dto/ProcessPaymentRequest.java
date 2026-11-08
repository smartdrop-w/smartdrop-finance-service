package com.smart.drop.finance.interfaces.rest.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;

@Schema(description = "Request to charge and process payment for a subscription")
public record ProcessPaymentRequest(
        @Schema(description = "Subscription identifier to pay for", example = "200")
        Integer subscriptionId,

        @Schema(description = "Payment amount to process", example = "99.90")
        BigDecimal amount,

        @Schema(description = "Currency ISO code", example = "USD")
        String currency,

        @Schema(description = "Payment method chosen (CREDIT_CARD, WALLET, BANK_TRANSFER)", example = "CREDIT_CARD")
        String method
) {
}
