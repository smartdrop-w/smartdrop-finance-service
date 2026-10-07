package com.smart.drop.finance.interfaces.rest;

import com.smart.drop.finance.application.services.WalletService;
import com.smart.drop.finance.domain.model.entities.Wallet;
import com.smart.drop.finance.domain.model.entities.WalletTransaction;
import com.smart.drop.finance.interfaces.rest.dto.FundsOperationRequest;
import com.smart.drop.finance.interfaces.rest.dto.WalletResponse;
import com.smart.drop.finance.interfaces.rest.dto.WalletTransactionResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/finance/wallet")
public class WalletController {

    private final WalletService walletService;

    public WalletController(WalletService walletService) {
        this.walletService = walletService;
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<WalletResponse> getWallet(@PathVariable Integer userId) {
        Wallet wallet = walletService.getOrCreateWallet(userId);
        return ResponseEntity.ok(toResponse(wallet));
    }

    @PostMapping("/user/{userId}/add-funds")
    public ResponseEntity<WalletResponse> addFunds(
            @PathVariable Integer userId,
            @RequestBody FundsOperationRequest request
    ) {
        Wallet wallet = walletService.addFunds(userId, request.amount(), request.transactionName());
        return ResponseEntity.ok(toResponse(wallet));
    }

    @PostMapping("/user/{userId}/withdraw")
    public ResponseEntity<WalletResponse> withdraw(
            @PathVariable Integer userId,
            @RequestBody FundsOperationRequest request
    ) {
        Wallet wallet = walletService.withdrawFunds(userId, request.amount(), request.transactionName());
        return ResponseEntity.ok(toResponse(wallet));
    }

    @GetMapping("/user/{userId}/transactions")
    public ResponseEntity<List<WalletTransactionResponse>> getTransactions(@PathVariable Integer userId) {
        List<WalletTransactionResponse> data = walletService.getTransactions(userId)
                .stream()
                .map(this::toResponse)
                .toList();
        return ResponseEntity.ok(data);
    }

    private WalletResponse toResponse(Wallet wallet) {
        return new WalletResponse(wallet.walletId(), wallet.userId(), wallet.balance());
    }

    private WalletTransactionResponse toResponse(WalletTransaction tx) {
        return new WalletTransactionResponse(
                tx.transactionId(),
                tx.walletId(),
                tx.name(),
                tx.amount(),
                tx.transactionDate()
        );
    }
}
