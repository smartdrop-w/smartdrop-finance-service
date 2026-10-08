package com.smart.drop.finance.interfaces.rest;

import com.smart.drop.finance.application.services.PaymentService;
import com.smart.drop.finance.domain.model.entities.Invoice;
import com.smart.drop.finance.domain.model.entities.Payment;
import com.smart.drop.finance.interfaces.rest.dto.InvoiceResponse;
import com.smart.drop.finance.interfaces.rest.dto.PaymentResponse;
import com.smart.drop.finance.interfaces.rest.dto.ProcessPaymentRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/finance/payments")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping("/charge")
    public ResponseEntity<PaymentResponse> charge(@RequestBody ProcessPaymentRequest request) {
        Payment payment = paymentService.processPayment(
                request.subscriptionId(),
                request.amount(),
                request.currency(),
                request.method()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(toPaymentResponse(payment));
    }

    @GetMapping("/subscription/{subscriptionId}")
    public ResponseEntity<List<PaymentResponse>> getPaymentsBySubscription(@PathVariable Integer subscriptionId) {
        List<PaymentResponse> data = paymentService.getPaymentsBySubscriptionId(subscriptionId)
                .stream()
                .map(this::toPaymentResponse)
                .toList();
        return ResponseEntity.ok(data);
    }

    @GetMapping("/subscription/{subscriptionId}/invoices")
    public ResponseEntity<List<InvoiceResponse>> getInvoicesBySubscription(@PathVariable Integer subscriptionId) {
        List<InvoiceResponse> data = paymentService.getInvoicesBySubscriptionId(subscriptionId)
                .stream()
                .map(this::toInvoiceResponse)
                .toList();
        return ResponseEntity.ok(data);
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

    private InvoiceResponse toInvoiceResponse(Invoice invoice) {
        return new InvoiceResponse(
                invoice.invoiceId(),
                invoice.subscriptionId(),
                invoice.issuedAt(),
                invoice.total(),
                invoice.currency()
        );
    }
}

