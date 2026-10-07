package com.smart.drop.finance.interfaces.rest;

import com.smart.drop.finance.application.services.WalletService;
import com.smart.drop.finance.domain.model.entities.PaymentMethod;
import com.smart.drop.finance.interfaces.rest.dto.CreatePaymentMethodRequest;
import com.smart.drop.finance.interfaces.rest.dto.PaymentMethodResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/finance/payment-methods")
public class PaymentMethodController {

    private final WalletService walletService;

    public PaymentMethodController(WalletService walletService) {
        this.walletService = walletService;
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<PaymentMethodResponse>> getPaymentMethods(@PathVariable Integer userId) {
        List<PaymentMethodResponse> data = walletService.getPaymentMethods(userId)
                .stream()
                .map(this::toResponse)
                .toList();
        return ResponseEntity.ok(data);
    }

    @PostMapping
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
    public ResponseEntity<PaymentMethodResponse> setPrimary(
            @PathVariable Integer methodId,
            @PathVariable Integer userId
    ) {
        PaymentMethod updated = walletService.setPrimaryPaymentMethod(userId, methodId);
        return ResponseEntity.ok(toResponse(updated));
    }

    @DeleteMapping("/{methodId}")
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
