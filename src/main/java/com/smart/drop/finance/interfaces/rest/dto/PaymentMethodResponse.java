package com.smart.drop.finance.interfaces.rest.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Registered payment method response details")
public record PaymentMethodResponse(
        @Schema(description = "Payment method identifier", example = "10")
        Integer paymentMethodId,

        @Schema(description = "User identifier owner", example = "42")
        Integer userId,

        @Schema(description = "Payment method type", example = "CREDIT_CARD")
        String type,

        @Schema(description = "User label for card/method", example = "Visa Corporate - BCP")
        String label,

        @Schema(description = "Masked card number or account identifier", example = "**** **** **** 4242")
        String sub,

        @Schema(description = "Whether this is the user's primary payment method", example = "true")
        Boolean primary
) {
}
