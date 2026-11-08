package com.smart.drop.finance.interfaces.rest;

import com.smart.drop.finance.application.services.WalletService;
import com.smart.drop.finance.domain.model.entities.PaymentMethod;
import com.smart.drop.finance.interfaces.rest.dto.CreatePaymentMethodRequest;
import com.smart.drop.finance.interfaces.rest.dto.PaymentMethodResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/finance/payment-methods")
@Tag(name = "Payment Methods", description = "Management of registered credit cards, bank accounts, and user payment options")
public class PaymentMethodController {

    private final WalletService walletService;

    public PaymentMethodController(WalletService walletService) {
        this.walletService = walletService;
    }

    @GetMapping("/user/{userId}")
    @Operation(summary = "Get user payment methods", description = "Retrieves all saved payment methods for a specific user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Payment methods retrieved successfully"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<List<PaymentMethodResponse>> getPaymentMethods(@PathVariable Integer userId) {
        List<PaymentMethodResponse> data = walletService.getPaymentMethods(userId)
                .stream()
                .map(this::toResponse)
                .toList();
        return ResponseEntity.ok(data);
    }

    @PostMapping
    @Operation(summary = "Register payment method", description = "Adds and validates a new payment method for a user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Payment method registered successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid payment method details"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<PaymentMethodResponse> create(@RequestBody CreatePaymentMethodRequest request) {
        PaymentMethod created = walletService.addPaymentMethod(
                request.userId(),
                request.type(),
                request.label(),
                request.sub(),
                request.primary()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(toResponse(created));
    }

    @PutMapping("/{methodId}/primary/user/{userId}")
    @Operation(summary = "Set primary payment method", description = "Designates a specific payment method as the default primary method")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Primary payment method set successfully"),
            @ApiResponse(responseCode = "404", description = "Payment method not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<PaymentMethodResponse> setPrimary(
            @PathVariable Integer methodId,
            @PathVariable Integer userId
    ) {
        PaymentMethod updated = walletService.setPrimaryPaymentMethod(userId, methodId);
        return ResponseEntity.ok(toResponse(updated));
    }

    @DeleteMapping("/{methodId}")
    @Operation(summary = "Delete payment method", description = "Removes a saved payment method from user profile")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Payment method deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Payment method not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Void> delete(@PathVariable Integer methodId) {
        walletService.deletePaymentMethod(methodId);
        return ResponseEntity.noContent().build();
    }

    private PaymentMethodResponse toResponse(PaymentMethod method) {
        return new PaymentMethodResponse(
                method.paymentMethodId(),
                method.userId(),
                method.type(),
                method.label(),
                method.sub(),
                method.primary()
        );
    }
}
