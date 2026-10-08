package com.smart.drop.finance.interfaces.rest.dto;

public record CreatePaymentMethodRequest(
        Integer userId,
        String type,
        String label,
        String sub,
        Boolean primary
) {
}
