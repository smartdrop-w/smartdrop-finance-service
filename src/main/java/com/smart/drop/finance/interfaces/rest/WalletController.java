package com.smart.drop.finance.interfaces.rest;

import com.smart.drop.finance.application.services.WalletService;
import com.smart.drop.finance.domain.model.entities.Wallet;
import com.smart.drop.finance.domain.model.entities.WalletTransaction;
import com.smart.drop.finance.interfaces.rest.dto.FundsOperationRequest;
import com.smart.drop.finance.interfaces.rest.dto.WalletResponse;
import com.smart.drop.finance.interfaces.rest.dto.WalletTransactionResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/finance/wallet")
@Tag(name = "Wallet", description = "Digital balance management, credit/debit funds recharge, and transaction ledger")
public class WalletController {

    private final WalletService walletService;

    public WalletController(WalletService walletService) {
        this.walletService = walletService;
    }

    @GetMapping("/user/{userId}")
    @Operation(summary = "Get user wallet", description = "Retrieves current digital wallet balance for a user, creating one if not present")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Wallet retrieved successfully"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<WalletResponse> getWallet(@PathVariable Integer userId) {
        Wallet wallet = walletService.getOrCreateWallet(userId);
        return ResponseEntity.ok(toResponse(wallet));
    }

    @PostMapping("/user/{userId}/add-funds")
    @Operation(summary = "Add funds to wallet", description = "Credits monetary amount into user digital wallet and logs a ledger transaction")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Funds added successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid deposit amount"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<WalletResponse> addFunds(
            @PathVariable Integer userId,
            @RequestBody FundsOperationRequest request
    ) {
        Wallet wallet = walletService.addFunds(userId, request.amount(), request.transactionName());
        return ResponseEntity.ok(toResponse(wallet));
    }

    @PostMapping("/user/{userId}/withdraw")
    @Operation(summary = "Withdraw funds from wallet", description = "Debits monetary amount from user digital wallet balance")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Funds withdrawn successfully"),
            @ApiResponse(responseCode = "400", description = "Insufficient funds or invalid amount"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<WalletResponse> withdraw(
            @PathVariable Integer userId,
            @RequestBody FundsOperationRequest request
    ) {
        Wallet wallet = walletService.withdrawFunds(userId, request.amount(), request.transactionName());
        return ResponseEntity.ok(toResponse(wallet));
    }

    @GetMapping("/user/{userId}/transactions")
    @Operation(summary = "Get wallet transactions", description = "Retrieves historical credit and debit balance movements for a user's wallet")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Transactions retrieved successfully"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
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
