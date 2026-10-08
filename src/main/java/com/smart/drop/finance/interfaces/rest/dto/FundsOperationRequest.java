package com.smart.drop.finance.interfaces.rest.dto;

import java.math.BigDecimal;

public record FundsOperationRequest(
        BigDecimal amount,
        String transactionName
) {
}
