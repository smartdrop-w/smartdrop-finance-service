package com.smart.drop.finance.interfaces.rest.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Schema(description = "Processed payment transaction details")
public record PaymentResponse(
        @Schema(description = "Payment transaction identifier", example = "3001")
        Integer paymentId,

        @Schema(description = "Associated subscription identifier", example = "200")
        Integer subscriptionId,

        @Schema(description = "Processed amount", example = "99.90")
        BigDecimal amount,

        @Schema(description = "Currency ISO code", example = "USD")
        String currency,

        @Schema(description = "Timestamp when payment succeeded", example = "2026-11-14T08:35:12")
        LocalDateTime paymentDate,

        @Schema(description = "Payment method used", example = "CREDIT_CARD")
        String method,

        @Schema(description = "External payment gateway transaction token or ID", example = "txn_live_9a8b7c6d5e4f")
        String transactionId
) {
}
