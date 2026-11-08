package com.smart.drop.finance.interfaces.rest;

import com.smart.drop.finance.application.services.PaymentService;
import com.smart.drop.finance.domain.model.entities.Invoice;
import com.smart.drop.finance.domain.model.entities.Payment;
import com.smart.drop.finance.interfaces.rest.dto.InvoiceResponse;
import com.smart.drop.finance.interfaces.rest.dto.PaymentResponse;
import com.smart.drop.finance.interfaces.rest.dto.ProcessPaymentRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Payments", description = "Payment transactions, payment processing, and billing invoice lookups")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping("/charge")
    @Operation(summary = "Process subscription payment", description = "Charges a payment method and registers a successful payment against a subscription")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Payment processed successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid payment parameters"),
            @ApiResponse(responseCode = "404", description = "Subscription not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
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
    @Operation(summary = "Get payments by subscription ID", description = "Retrieves all payment transactions executed for a subscription")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Payments retrieved successfully"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<List<PaymentResponse>> getPaymentsBySubscription(@PathVariable Integer subscriptionId) {
        List<PaymentResponse> data = paymentService.getPaymentsBySubscriptionId(subscriptionId)
                .stream()
                .map(this::toPaymentResponse)
                .toList();
        return ResponseEntity.ok(data);
    }

    @GetMapping("/subscription/{subscriptionId}/invoices")
    @Operation(summary = "Get invoices by subscription ID", description = "Retrieves all issued invoices for a subscription")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Invoices retrieved successfully"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<List<InvoiceResponse>> getInvoicesBySubscription(@PathVariable Integer subscriptionId) {
        List<InvoiceResponse> data = paymentService.getInvoicesBySubscriptionId(subscriptionId)
                .stream()
                .map(this::toInvoiceResponse)
                .toList();
        return ResponseEntity.ok(data);
    }

    @GetMapping("/invoices/{invoiceId}")
    @Operation(summary = "Get invoice by ID", description = "Retrieves an invoice by its unique billing identifier")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Invoice found"),
            @ApiResponse(responseCode = "404", description = "Invoice not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<InvoiceResponse> getInvoiceById(@PathVariable Integer invoiceId) {
        return paymentService.getInvoiceById(invoiceId)
                .map(inv -> ResponseEntity.ok(toInvoiceResponse(inv)))
                .orElseGet(() -> ResponseEntity.notFound().build());
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
