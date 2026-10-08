package com.smart.drop.finance.interfaces.rest.dto;

import java.math.BigDecimal;

public record ProcessPaymentRequest(
        Integer subscriptionId,
        BigDecimal amount,
        String currency,
        String method
) {
}

