package com.smart.drop.finance.interfaces.rest.dto;

import java.time.LocalDate;
import java.util.List;

public record SubscriptionResponse(
        Integer subscriptionId,
        Integer userId,
        Integer planId,
        String status,
        LocalDate startDate,
        LocalDate endDate,
        Boolean autoRenew,
        SubscriptionPlanResponse plan,
        List<InvoiceResponse> invoices,
        List<PaymentResponse> payments
) {
}

