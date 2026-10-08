package com.smart.drop.finance.interfaces.rest.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record InvoiceResponse(
        Integer invoiceId,
        Integer subscriptionId,
        LocalDateTime issuedAt,
        BigDecimal total,
        String currency
) {
}

