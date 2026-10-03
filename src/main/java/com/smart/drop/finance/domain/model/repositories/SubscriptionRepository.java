package com.smart.drop.finance.domain.model.repositories;

import com.smart.drop.finance.domain.model.aggregates.Subscription;
import com.smart.drop.finance.domain.model.entities.Invoice;
import com.smart.drop.finance.domain.model.entities.Payment;
import com.smart.drop.finance.domain.model.entities.SubscriptionPlan;

import java.util.List;
import java.util.Optional;

/**
 * Domain repository for subscriptions and related financial records.
 */
public interface SubscriptionRepository {

    SubscriptionPlan savePlan(SubscriptionPlan plan);

    Optional<SubscriptionPlan> findPlanById(Integer planId);

    List<SubscriptionPlan> findAllPlans();

    Subscription saveSubscription(Subscription subscription);

    Subscription updateSubscription(Subscription subscription);

    Optional<Subscription> findSubscriptionById(Integer subscriptionId);

    List<Subscription> findSubscriptionsByUserId(Integer userId);

    Payment savePayment(Payment payment);

    List<Payment> findPaymentsBySubscriptionId(Integer subscriptionId);

    Invoice saveInvoice(Invoice invoice);

    List<Invoice> findInvoicesBySubscriptionId(Integer subscriptionId);
}

