package com.smart.drop.finance.interfaces.rest.dto;

public record PaymentMethodResponse(
        Integer paymentMethodId,
        Integer userId,
        String type,
        String label,
        String sub,
        Boolean primary
) {
}
