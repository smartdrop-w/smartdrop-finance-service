package com.smart.drop.finance.interfaces.rest;

import com.smart.drop.finance.application.services.PaymentService;
import com.smart.drop.finance.domain.model.aggregates.Subscription;
import com.smart.drop.finance.domain.model.entities.Invoice;
import com.smart.drop.finance.domain.model.entities.Payment;
import com.smart.drop.finance.domain.model.entities.SubscriptionPlan;
import com.smart.drop.finance.interfaces.rest.dto.CreateSubscriptionPlanRequest;
import com.smart.drop.finance.interfaces.rest.dto.CreateSubscriptionRequest;
import com.smart.drop.finance.interfaces.rest.dto.InvoiceResponse;
import com.smart.drop.finance.interfaces.rest.dto.PaymentResponse;
import com.smart.drop.finance.interfaces.rest.dto.SubscriptionPlanResponse;
import com.smart.drop.finance.interfaces.rest.dto.SubscriptionResponse;
import com.smart.drop.finance.interfaces.rest.dto.ToggleAutoRenewRequest;
import com.smart.drop.finance.interfaces.rest.dto.UpdateSubscriptionStatusRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping({"/api/v1/finance/subscriptions", "/api/smartdrop/finance/subscriptions"})
@Tag(name = "Subscriptions", description = "Subscription plans, customer subscriptions lifecycle, and auto-renewal management")
public class SubscriptionController {

    private final PaymentService paymentService;

    public SubscriptionController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping("/plans")
    @Operation(summary = "Create subscription plan tier", description = "Creates a new subscription plan offering with pricing and duration")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Plan created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request payload"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<SubscriptionPlanResponse> createPlan(@RequestBody CreateSubscriptionPlanRequest request) {
        SubscriptionPlan plan = paymentService.createPlan(
                request.name(),
                request.description(),
                request.price(),
                request.duration()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(toPlanResponse(plan));
    }

    @GetMapping("/plans")
    @Operation(summary = "List all subscription plans", description = "Retrieves all available subscription plans")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Plans retrieved successfully"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<List<SubscriptionPlanResponse>> getPlans() {
        List<SubscriptionPlanResponse> data = paymentService.getAllPlans().stream().map(this::toPlanResponse).toList();
        return ResponseEntity.ok(data);
    }

    @GetMapping("/plans/{planId}")
    @Operation(summary = "Get subscription plan by ID", description = "Retrieves a specific subscription plan by its identifier")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Plan found"),
            @ApiResponse(responseCode = "404", description = "Plan not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<SubscriptionPlanResponse> getPlanById(@PathVariable Integer planId) {
        SubscriptionPlan plan = paymentService.getPlanById(planId);
        return ResponseEntity.ok(toPlanResponse(plan));
    }

    @PostMapping
    @Operation(summary = "Create user subscription", description = "Subscribes a customer to a specified plan tier")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Subscription created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid subscription parameters"),
            @ApiResponse(responseCode = "404", description = "Plan or user not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<SubscriptionResponse> createSubscription(@RequestBody CreateSubscriptionRequest request) {
        Subscription subscription = paymentService.createSubscription(
                request.userId(),
                request.planId(),
                request.autoRenew()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(toSubscriptionResponse(subscription));
    }

    @GetMapping("/{subscriptionId}")
    @Operation(summary = "Get subscription by ID", description = "Retrieves detailed subscription information including invoices and payments")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Subscription found"),
            @ApiResponse(responseCode = "404", description = "Subscription not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<SubscriptionResponse> getSubscription(@PathVariable Integer subscriptionId) {
        Subscription subscription = paymentService.getSubscriptionById(subscriptionId);
        return ResponseEntity.ok(toSubscriptionResponse(subscription));
    }

    @GetMapping("/user/{userId}")
    @Operation(summary = "Get subscriptions by user ID", description = "Retrieves all subscriptions owned by a specific user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Subscriptions retrieved successfully"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<List<SubscriptionResponse>> getByUser(@PathVariable Integer userId) {
        List<SubscriptionResponse> data = paymentService.getSubscriptionsByUserId(userId)
                .stream()
                .map(this::toSubscriptionResponse)
                .toList();
        return ResponseEntity.ok(data);
    }

    @PutMapping("/{subscriptionId}/status")
    @Operation(summary = "Update subscription status", description = "Modifies subscription lifecycle status (ACTIVE, SUSPENDED, CANCELLED)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Subscription status updated successfully"),
            @ApiResponse(responseCode = "404", description = "Subscription not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<SubscriptionResponse> updateStatus(
            @PathVariable Integer subscriptionId,
            @RequestBody UpdateSubscriptionStatusRequest request
    ) {
        Subscription updated = paymentService.updateSubscriptionStatus(subscriptionId, request.status());
        return ResponseEntity.ok(toSubscriptionResponse(updated));
    }

    @PutMapping("/{subscriptionId}/auto-renew")
    @Operation(summary = "Toggle subscription auto-renewal", description = "Enables, disables, or flips automatic renewal for a subscription")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Auto-renewal updated successfully"),
            @ApiResponse(responseCode = "404", description = "Subscription not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<SubscriptionResponse> toggleAutoRenew(
            @PathVariable Integer subscriptionId,
            @RequestBody(required = false) ToggleAutoRenewRequest request
    ) {
        Subscription updated = paymentService.toggleAutoRenew(
                subscriptionId,
                request != null ? request.autoRenew() : null
        );
        return ResponseEntity.ok(toSubscriptionResponse(updated));
    }

    private SubscriptionPlanResponse toPlanResponse(SubscriptionPlan plan) {
        return new SubscriptionPlanResponse(
                plan.planId(),
                plan.name(),
                plan.description(),
                plan.price(),
                plan.duration(),
                plan.createdAt()
        );
    }

    private SubscriptionResponse toSubscriptionResponse(Subscription subscription) {
        SubscriptionPlanResponse plan = subscription.plan() == null ? null : toPlanResponse(subscription.plan());
        List<InvoiceResponse> invoices = subscription.invoices().stream().map(this::toInvoiceResponse).toList();
        List<PaymentResponse> payments = subscription.payments().stream().map(this::toPaymentResponse).toList();

        return new SubscriptionResponse(
                subscription.subscriptionId(),
                subscription.userId(),
                subscription.planId(),
                subscription.status(),
                subscription.startDate(),
                subscription.endDate(),
                subscription.autoRenew(),
                plan,
                invoices,
                payments
        );
    }

    private InvoiceResponse toInvoiceResponse(Invoice invoice) {
        return new InvoiceResponse(
                invoice.invoiceId(),
                invoice.subscriptionId(),
                invoice.issuedAt(),
                invoice.total(),
                invoice.currency()
        );
    }

    private PaymentResponse toPaymentResponse(Payment payment) {
        return new PaymentResponse(
                payment.paymentId(),
                payment.subscriptionId(),
                payment.amount(),
                payment.currency(),
                payment.paymentDate(),
                payment.method(),
                payment.transactionId()
        );
    }
}
