package com.smart.drop.finance.domain.model.aggregates;

import com.smart.drop.finance.domain.model.entities.Invoice;
import com.smart.drop.finance.domain.model.entities.Payment;
import com.smart.drop.finance.domain.model.entities.SubscriptionPlan;

import java.time.LocalDate;
import java.util.List;

/**
 * Aggregate root for subscriptions.
 */
public record Subscription(
        Integer subscriptionId,
        Integer userId,
        Integer planId,
        String status,
        LocalDate startDate,
        LocalDate endDate,
        Boolean autoRenew,
        SubscriptionPlan plan,
        List<Invoice> invoices,
        List<Payment> payments
) {
    public Subscription {
        invoices = invoices == null ? List.of() : List.copyOf(invoices);
        payments = payments == null ? List.of() : List.copyOf(payments);
        if (status != null && status.length() > 20) {
            throw new IllegalArgumentException("status must be <= 20 characters");
        }
    }

    public static Subscription create(Integer userId, Integer planId, Integer durationDays, Boolean autoRenew) {
        LocalDate start = LocalDate.now();
        LocalDate end = durationDays == null ? null : start.plusDays(durationDays);
        return new Subscription(
                null,
                userId,
                planId,
                "PENDING",
                start,
                end,
                autoRenew,
                null,
                List.of(),
                List.of()
        );
    }

    public Subscription withStatus(String newStatus) {
        return new Subscription(
                subscriptionId,
                userId,
                planId,
                newStatus,
                startDate,
                endDate,
                autoRenew,
                plan,
                invoices,
                payments
        );
    }

    public Subscription withPlan(SubscriptionPlan targetPlan) {
        return new Subscription(
                subscriptionId,
                userId,
                planId,
                status,
                startDate,
                endDate,
                autoRenew,
                targetPlan,
                invoices,
                payments
        );
    }

    public Subscription withFinancialData(List<Invoice> targetInvoices, List<Payment> targetPayments) {
        return new Subscription(
                subscriptionId,
                userId,
                planId,
                status,
                startDate,
                endDate,
                autoRenew,
                plan,
                targetInvoices,
                targetPayments
        );
    }
}

