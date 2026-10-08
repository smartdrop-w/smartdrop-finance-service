package com.smart.drop.finance.application.services;

import com.smart.drop.finance.domain.model.aggregates.Subscription;
import com.smart.drop.finance.domain.model.entities.Invoice;
import com.smart.drop.finance.domain.model.entities.Payment;
import com.smart.drop.finance.domain.model.entities.SubscriptionPlan;
import com.smart.drop.finance.domain.model.ports.PaymentGatewayPort;
import com.smart.drop.finance.domain.model.repositories.SubscriptionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@Transactional
public class PaymentService {

    private final SubscriptionRepository subscriptionRepository;
    private final PaymentGatewayPort paymentGatewayPort;

    public PaymentService(SubscriptionRepository subscriptionRepository,
                          PaymentGatewayPort paymentGatewayPort) {
        this.subscriptionRepository = subscriptionRepository;
        this.paymentGatewayPort = paymentGatewayPort;
    }

    public SubscriptionPlan createPlan(String name, String description, BigDecimal price, Integer duration) {
        SubscriptionPlan plan = SubscriptionPlan.create(name, description, price, duration);
        return subscriptionRepository.savePlan(plan);
    }

    @Transactional(readOnly = true)
    public List<SubscriptionPlan> getAllPlans() {
        return subscriptionRepository.findAllPlans();
    }

    @Transactional(readOnly = true)
    public SubscriptionPlan getPlanById(Integer planId) {
        return subscriptionRepository.findPlanById(planId)
                .orElseThrow(() -> new IllegalArgumentException("Plan not found: " + planId));
    }

    public Subscription createSubscription(Integer userId, Integer planId, Boolean autoRenew) {
        SubscriptionPlan plan = getPlanById(planId);
        Subscription subscription = Subscription.create(userId, planId, plan.duration(), autoRenew).withPlan(plan);
        return subscriptionRepository.saveSubscription(subscription);
    }

    @Transactional(readOnly = true)
    public Subscription getSubscriptionById(Integer subscriptionId) {
        return subscriptionRepository.findSubscriptionById(subscriptionId)
                .orElseThrow(() -> new IllegalArgumentException("Subscription not found: " + subscriptionId));
    }

    @Transactional(readOnly = true)
    public List<Subscription> getSubscriptionsByUserId(Integer userId) {
        return subscriptionRepository.findSubscriptionsByUserId(userId);
    }

    public Subscription updateSubscriptionStatus(Integer subscriptionId, String status) {
        Subscription subscription = getSubscriptionById(subscriptionId);
        Subscription updated = subscription.withStatus(status);
        return subscriptionRepository.updateSubscription(updated);
    }

    public Payment processPayment(Integer subscriptionId, BigDecimal amount, String currency, String method) {
        Subscription subscription = getSubscriptionById(subscriptionId);

        PaymentGatewayPort.PaymentGatewayResponse gatewayResponse = paymentGatewayPort.charge(
                new PaymentGatewayPort.PaymentGatewayRequest(subscriptionId, amount, currency, method)
        );

        if (!gatewayResponse.success()) {
            throw new IllegalStateException("Payment rejected by provider: " + gatewayResponse.providerMessage());
        }

        Payment payment = Payment.create(
                subscriptionId,
                amount,
                currency,
                method,
                gatewayResponse.transactionId()
        );

        Payment savedPayment = subscriptionRepository.savePayment(payment);

        Invoice invoice = Invoice.create(subscriptionId, amount, currency);
        subscriptionRepository.saveInvoice(invoice);

        if (!"ACTIVE".equalsIgnoreCase(subscription.status())) {
            subscriptionRepository.updateSubscription(subscription.withStatus("ACTIVE"));
        }

        return savedPayment;
    }

    @Transactional(readOnly = true)
    public List<Payment> getPaymentsBySubscriptionId(Integer subscriptionId) {
        return subscriptionRepository.findPaymentsBySubscriptionId(subscriptionId);
    }

    @Transactional(readOnly = true)
    public List<Invoice> getInvoicesBySubscriptionId(Integer subscriptionId) {
        return subscriptionRepository.findInvoicesBySubscriptionId(subscriptionId);
    }
}

