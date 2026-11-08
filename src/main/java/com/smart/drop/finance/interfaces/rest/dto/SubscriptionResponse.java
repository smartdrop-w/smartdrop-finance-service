package com.smart.drop.finance.interfaces.rest.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;
import java.util.List;

@Schema(description = "User subscription complete details with plan, billing invoices, and payment history")
public record SubscriptionResponse(
        @Schema(description = "Subscription identifier", example = "200")
        Integer subscriptionId,

        @Schema(description = "Owner user identifier", example = "42")
        Integer userId,

        @Schema(description = "Associated plan identifier", example = "1")
        Integer planId,

        @Schema(description = "Current lifecycle status (ACTIVE, PENDING, CANCELLED, EXPIRED)", example = "ACTIVE")
        String status,

        @Schema(description = "Subscription cycle start date", example = "2026-11-01")
        LocalDate startDate,

        @Schema(description = "Subscription cycle expiration date", example = "2026-12-01")
        LocalDate endDate,

        @Schema(description = "Whether automatic renewal is enabled", example = "true")
        Boolean autoRenew,

        @Schema(description = "Subscription plan tier details")
        SubscriptionPlanResponse plan,

        @Schema(description = "Invoices issued under this subscription")
        List<InvoiceResponse> invoices,

        @Schema(description = "Payment history recorded for this subscription")
        List<PaymentResponse> payments
) {
}
