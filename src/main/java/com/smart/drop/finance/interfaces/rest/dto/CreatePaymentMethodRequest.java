package com.smart.drop.finance.interfaces.rest.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Request to register a new payment method for a user")
public record CreatePaymentMethodRequest(
        @Schema(description = "Identifier of the user owning the payment method", example = "42")
        Integer userId,

        @Schema(description = "Payment method type (CREDIT_CARD, DEBIT_CARD, BANK_TRANSFER, DIGITAL_WALLET)", example = "CREDIT_CARD")
        String type,

        @Schema(description = "Display label or bank name for identification", example = "Visa Corporate - BCP")
        String label,

        @Schema(description = "Masked number or card identifier summary", example = "**** **** **** 4242")
        String sub,

        @Schema(description = "Whether this payment method is the default primary method", example = "true")
        Boolean primary
) {
}
