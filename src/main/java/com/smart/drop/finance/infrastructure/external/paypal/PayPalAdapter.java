package com.smart.drop.finance.infrastructure.external.paypal;

import com.smart.drop.finance.domain.model.ports.PaymentGatewayPort;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * External adapter for PayPal integration.
 * This mock implementation simulates a successful charge.
 */
@Component
public class PayPalAdapter implements PaymentGatewayPort {

    @Override
    public PaymentGatewayResponse charge(PaymentGatewayRequest request) {
        String transactionId = "PAYPAL-" + request.subscriptionId() + "-" +
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));

        return new PaymentGatewayResponse(true, transactionId, "Approved by PayPal mock adapter");
    }
}

