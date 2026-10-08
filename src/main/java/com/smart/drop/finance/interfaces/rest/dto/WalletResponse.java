package com.smart.drop.finance.interfaces.rest.dto;

import java.math.BigDecimal;

public record WalletResponse(
        Integer walletId,
        Integer userId,
        BigDecimal balance
) {
}
