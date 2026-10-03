package com.smart.drop.finance.domain.model.ports;

import java.math.BigDecimal;

/**
 * Port for external payment providers.
 */
public interface PaymentGatewayPort {

    PaymentGatewayResponse charge(PaymentGatewayRequest request);

    record PaymentGatewayRequest(
            Integer subscriptionId,
            BigDecimal amount,
            String currency,
            String method
    ) {
    }

    record PaymentGatewayResponse(
            boolean success,
            String transactionId,
            String providerMessage
    ) {
    }
}

