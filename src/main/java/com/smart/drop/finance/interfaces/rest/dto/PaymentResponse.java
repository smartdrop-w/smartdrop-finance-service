package com.smart.drop.finance.interfaces.rest.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record PaymentResponse(
        Integer paymentId,
        Integer subscriptionId,
        BigDecimal amount,
        String currency,
        LocalDateTime paymentDate,
        String method,
        String transactionId
) {
}

