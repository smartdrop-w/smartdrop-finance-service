package com.smart.drop.finance.application.services;

import com.smart.drop.finance.domain.model.entities.Wallet;
import com.smart.drop.finance.domain.model.entities.PaymentMethod;
import com.smart.drop.finance.domain.model.entities.WalletTransaction;
import com.smart.drop.finance.domain.model.repositories.FinanceRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class WalletService {

    private final FinanceRepository financeRepository;

    public WalletService(FinanceRepository financeRepository) {
        this.financeRepository = financeRepository;
    }

    public Wallet getOrCreateWallet(Integer userId) {
        return financeRepository.findWalletByUserId(userId)
                .orElseGet(() -> financeRepository.saveWallet(Wallet.create(userId, BigDecimal.ZERO)));
    }

    public Wallet addFunds(Integer userId, BigDecimal amount, String name) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Amount must be positive");
        }
        Wallet wallet = getOrCreateWallet(userId);
        Wallet updatedWallet = new Wallet(wallet.walletId(), wallet.userId(), wallet.balance().add(amount));
        financeRepository.saveWallet(updatedWallet);

        WalletTransaction tx = WalletTransaction.create(wallet.walletId(), name, amount);
        financeRepository.saveTransaction(tx);

        return updatedWallet;
    }

    public Wallet withdrawFunds(Integer userId, BigDecimal amount, String name) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Amount must be positive");
        }
        Wallet wallet = getOrCreateWallet(userId);
        if (wallet.balance().compareTo(amount) < 0) {
            throw new IllegalArgumentException("Insufficient funds in wallet");
        }
        Wallet updatedWallet = new Wallet(wallet.walletId(), wallet.userId(), wallet.balance().subtract(amount));
        financeRepository.saveWallet(updatedWallet);

        // Withdrawal is represented as a negative amount for transaction history
        WalletTransaction tx = WalletTransaction.create(wallet.walletId(), name, amount.negate());
        financeRepository.saveTransaction(tx);

        return updatedWallet;
    }

    @Transactional(readOnly = true)
    public List<WalletTransaction> getTransactions(Integer userId) {
        Wallet wallet = getOrCreateWallet(userId);
        return financeRepository.findTransactionsByWalletId(wallet.walletId());
    }

    @Transactional(readOnly = true)
    public List<PaymentMethod> getPaymentMethods(Integer userId) {
        return financeRepository.findPaymentMethodsByUserId(userId);
    }

    public PaymentMethod addPaymentMethod(Integer userId, String type, String label, String sub, Boolean primary) {
        if (Boolean.TRUE.equals(primary)) {
            // Remove primary status from other payment methods
            List<PaymentMethod> existing = financeRepository.findPaymentMethodsByUserId(userId);
            for (PaymentMethod method : existing) {
                if (Boolean.TRUE.equals(method.primary())) {
                    financeRepository.savePaymentMethod(new PaymentMethod(
                            method.paymentMethodId(),
                            method.userId(),
                            method.type(),
                            method.label(),
                            method.sub(),
                            false
                    ));
                }
            }
        }

        PaymentMethod method = PaymentMethod.create(userId, type, label, sub, primary);
        return financeRepository.savePaymentMethod(method);
    }

    public PaymentMethod setPrimaryPaymentMethod(Integer userId, Integer paymentMethodId) {
        List<PaymentMethod> existing = financeRepository.findPaymentMethodsByUserId(userId);
        PaymentMethod primaryMethod = null;

        for (PaymentMethod method : existing) {
            boolean isTarget = method.paymentMethodId().equals(paymentMethodId);
            PaymentMethod updated = new PaymentMethod(
                    method.paymentMethodId(),
                    method.userId(),
                    method.type(),
                    method.label(),
                    method.sub(),
                    isTarget
            );
            financeRepository.savePaymentMethod(updated);
            if (isTarget) {
                primaryMethod = updated;
            }
        }

        if (primaryMethod == null) {
            throw new IllegalArgumentException("Payment method not found: " + paymentMethodId);
        }

        return primaryMethod;
    }

    public void deletePaymentMethod(Integer paymentMethodId) {
        financeRepository.deletePaymentMethodById(paymentMethodId);
    }
}
