package com.smart.drop.finance.infrastructure.persistence.jpa.adapters;

import com.smart.drop.finance.domain.model.aggregates.Subscription;
import com.smart.drop.finance.domain.model.entities.Invoice;
import com.smart.drop.finance.domain.model.entities.Payment;
import com.smart.drop.finance.domain.model.entities.SubscriptionPlan;
import com.smart.drop.finance.domain.model.repositories.SubscriptionRepository;
import com.smart.drop.finance.infrastructure.persistence.jpa.entities.InvoiceEntity;
import com.smart.drop.finance.infrastructure.persistence.jpa.entities.PaymentEntity;
import com.smart.drop.finance.infrastructure.persistence.jpa.entities.SubscriptionEntity;
import com.smart.drop.finance.infrastructure.persistence.jpa.entities.SubscriptionPlanEntity;
import com.smart.drop.finance.infrastructure.persistence.jpa.repositories.InvoiceJpaRepository;
import com.smart.drop.finance.infrastructure.persistence.jpa.repositories.PaymentJpaRepository;
import com.smart.drop.finance.infrastructure.persistence.jpa.repositories.SubscriptionJpaRepository;
import com.smart.drop.finance.infrastructure.persistence.jpa.repositories.SubscriptionPlanJpaRepository;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Component
public class SubscriptionRepositoryAdapter implements SubscriptionRepository {

    private final SubscriptionPlanJpaRepository subscriptionPlanJpaRepository;
    private final SubscriptionJpaRepository subscriptionJpaRepository;
    private final InvoiceJpaRepository invoiceJpaRepository;
    private final PaymentJpaRepository paymentJpaRepository;

    public SubscriptionRepositoryAdapter(SubscriptionPlanJpaRepository subscriptionPlanJpaRepository,
                                         SubscriptionJpaRepository subscriptionJpaRepository,
                                         InvoiceJpaRepository invoiceJpaRepository,
                                         PaymentJpaRepository paymentJpaRepository) {
        this.subscriptionPlanJpaRepository = subscriptionPlanJpaRepository;
        this.subscriptionJpaRepository = subscriptionJpaRepository;
        this.invoiceJpaRepository = invoiceJpaRepository;
        this.paymentJpaRepository = paymentJpaRepository;
    }

    @Override
    public SubscriptionPlan savePlan(SubscriptionPlan plan) {
        SubscriptionPlanEntity entity = new SubscriptionPlanEntity();
        entity.setPlanId(plan.planId());
        entity.setName(plan.name());
        entity.setDescription(plan.description());
        entity.setPrice(plan.price());
        entity.setDuration(plan.duration());
        entity.setCreatedAt(plan.createdAt() == null ? LocalDateTime.now() : plan.createdAt());
        SubscriptionPlanEntity saved = subscriptionPlanJpaRepository.save(entity);
        return toPlanDomain(saved);
    }

    @Override
    public Optional<SubscriptionPlan> findPlanById(Integer planId) {
        return subscriptionPlanJpaRepository.findById(planId).map(this::toPlanDomain);
    }

    @Override
    public List<SubscriptionPlan> findAllPlans() {
        return subscriptionPlanJpaRepository.findAll().stream().map(this::toPlanDomain).toList();
    }

    @Override
    public Subscription saveSubscription(Subscription subscription) {
        SubscriptionEntity entity = new SubscriptionEntity();
        entity.setSubscriptionId(subscription.subscriptionId());
        entity.setUserId(subscription.userId());
        entity.setStatus(subscription.status());
        entity.setStartDate(subscription.startDate());
        entity.setEndDate(subscription.endDate());
        entity.setAutoRenew(subscription.autoRenew());

        SubscriptionPlanEntity planEntity = subscriptionPlanJpaRepository.findById(subscription.planId())
                .orElseThrow(() -> new IllegalArgumentException("Plan not found: " + subscription.planId()));
        entity.setPlan(planEntity);

        SubscriptionEntity saved = subscriptionJpaRepository.save(entity);
        return toSubscriptionDomain(saved);
    }

    @Override
    public Subscription updateSubscription(Subscription subscription) {
        SubscriptionEntity existing = subscriptionJpaRepository.findById(subscription.subscriptionId())
                .orElseThrow(() -> new IllegalArgumentException("Subscription not found: " + subscription.subscriptionId()));

        existing.setUserId(subscription.userId());
        existing.setStatus(subscription.status());
        existing.setStartDate(subscription.startDate());
        existing.setEndDate(subscription.endDate());
        existing.setAutoRenew(subscription.autoRenew());

        if (subscription.planId() != null &&
                (existing.getPlan() == null || !subscription.planId().equals(existing.getPlan().getPlanId()))) {
            SubscriptionPlanEntity planEntity = subscriptionPlanJpaRepository.findById(subscription.planId())
                    .orElseThrow(() -> new IllegalArgumentException("Plan not found: " + subscription.planId()));
            existing.setPlan(planEntity);
        }

        SubscriptionEntity updated = subscriptionJpaRepository.save(existing);
        return toSubscriptionDomain(updated);
    }

    @Override
    public Optional<Subscription> findSubscriptionById(Integer subscriptionId) {
        return subscriptionJpaRepository.findById(subscriptionId).map(this::toSubscriptionDomain);
    }

    @Override
    public List<Subscription> findSubscriptionsByUserId(Integer userId) {
        return subscriptionJpaRepository.findByUserId(userId).stream().map(this::toSubscriptionDomain).toList();
    }

    @Override
    public Payment savePayment(Payment payment) {
        SubscriptionEntity subscriptionEntity = subscriptionJpaRepository.findById(payment.subscriptionId())
                .orElseThrow(() -> new IllegalArgumentException("Subscription not found: " + payment.subscriptionId()));

        PaymentEntity entity = new PaymentEntity();
        entity.setPaymentId(payment.paymentId());
        entity.setSubscription(subscriptionEntity);
        entity.setAmount(payment.amount());
        entity.setCurrency(payment.currency());
        entity.setPaymentDate(payment.paymentDate());
        entity.setMethod(payment.method());
        entity.setTransactionId(payment.transactionId());

        PaymentEntity saved = paymentJpaRepository.save(entity);
        return toPaymentDomain(saved);
    }

    @Override
    public List<Payment> findPaymentsBySubscriptionId(Integer subscriptionId) {
        return paymentJpaRepository.findBySubscription_SubscriptionId(subscriptionId).stream()
                .map(this::toPaymentDomain)
                .toList();
    }

    @Override
    public Invoice saveInvoice(Invoice invoice) {
        SubscriptionEntity subscriptionEntity = subscriptionJpaRepository.findById(invoice.subscriptionId())
                .orElseThrow(() -> new IllegalArgumentException("Subscription not found: " + invoice.subscriptionId()));

        InvoiceEntity entity = new InvoiceEntity();
        entity.setInvoiceId(invoice.invoiceId());
        entity.setSubscription(subscriptionEntity);
        entity.setIssuedAt(invoice.issuedAt());
        entity.setTotal(invoice.total());
        entity.setCurrency(invoice.currency());

        InvoiceEntity saved = invoiceJpaRepository.save(entity);
        return toInvoiceDomain(saved);
    }

    @Override
    public List<Invoice> findInvoicesBySubscriptionId(Integer subscriptionId) {
        return invoiceJpaRepository.findBySubscription_SubscriptionId(subscriptionId).stream()
                .map(this::toInvoiceDomain)
                .toList();
    }

    private SubscriptionPlan toPlanDomain(SubscriptionPlanEntity entity) {
        return new SubscriptionPlan(
                entity.getPlanId(),
                entity.getName(),
                entity.getDescription(),
                entity.getPrice(),
                entity.getDuration(),
                entity.getCreatedAt()
        );
    }

    private Subscription toSubscriptionDomain(SubscriptionEntity entity) {
        SubscriptionPlan plan = entity.getPlan() == null ? null : toPlanDomain(entity.getPlan());
        List<Payment> payments = paymentJpaRepository.findBySubscription_SubscriptionId(entity.getSubscriptionId())
                .stream()
                .map(this::toPaymentDomain)
                .toList();
        List<Invoice> invoices = invoiceJpaRepository.findBySubscription_SubscriptionId(entity.getSubscriptionId())
                .stream()
                .map(this::toInvoiceDomain)
                .toList();

        return new Subscription(
                entity.getSubscriptionId(),
                entity.getUserId(),
                entity.getPlan() == null ? null : entity.getPlan().getPlanId(),
                entity.getStatus(),
                entity.getStartDate(),
                entity.getEndDate(),
                entity.getAutoRenew(),
                plan,
                invoices,
                payments
        );
    }

    private Payment toPaymentDomain(PaymentEntity entity) {
        return new Payment(
                entity.getPaymentId(),
                entity.getSubscription() == null ? null : entity.getSubscription().getSubscriptionId(),
                entity.getAmount(),
                entity.getCurrency(),
                entity.getPaymentDate(),
                entity.getMethod(),
                entity.getTransactionId()
        );
    }

    private Invoice toInvoiceDomain(InvoiceEntity entity) {
        return new Invoice(
                entity.getInvoiceId(),
                entity.getSubscription() == null ? null : entity.getSubscription().getSubscriptionId(),
                entity.getIssuedAt(),
                entity.getTotal(),
                entity.getCurrency()
        );
    }
}

