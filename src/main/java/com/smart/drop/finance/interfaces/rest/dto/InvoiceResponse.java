package com.smart.drop.finance.interfaces.rest.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Schema(description = "Invoice billing record associated with a subscription")
public record InvoiceResponse(
        @Schema(description = "Unique invoice identifier", example = "5001")
        Integer invoiceId,

        @Schema(description = "Associated subscription identifier", example = "200")
        Integer subscriptionId,

        @Schema(description = "Timestamp when invoice was generated and issued", example = "2026-11-14T08:30:00")
        LocalDateTime issuedAt,

        @Schema(description = "Total billing amount", example = "99.90")
        BigDecimal total,

        @Schema(description = "Currency ISO code", example = "USD")
        String currency
) {
}
