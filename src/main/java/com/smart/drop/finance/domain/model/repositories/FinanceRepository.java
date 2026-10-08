package com.smart.drop.finance.domain.model.repositories;

import com.smart.drop.finance.domain.model.entities.Wallet;
import com.smart.drop.finance.domain.model.entities.PaymentMethod;
import com.smart.drop.finance.domain.model.entities.WalletTransaction;

import java.util.List;
import java.util.Optional;

/**
 * Domain repository contract for wallet and extra payment records.
 */
public interface FinanceRepository {

    Wallet saveWallet(Wallet wallet);

    Optional<Wallet> findWalletByUserId(Integer userId);

    PaymentMethod savePaymentMethod(PaymentMethod paymentMethod);

    Optional<PaymentMethod> findPaymentMethodById(Integer paymentMethodId);

    List<PaymentMethod> findPaymentMethodsByUserId(Integer userId);

    void deletePaymentMethodById(Integer paymentMethodId);

    WalletTransaction saveTransaction(WalletTransaction transaction);

    List<WalletTransaction> findTransactionsByWalletId(Integer walletId);
}
