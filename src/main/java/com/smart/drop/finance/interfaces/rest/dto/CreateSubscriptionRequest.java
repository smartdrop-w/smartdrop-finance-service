package com.smart.drop.finance.interfaces.rest.dto;

public record CreateSubscriptionRequest(
        Integer userId,
        Integer planId,
        Boolean autoRenew
) {
}

