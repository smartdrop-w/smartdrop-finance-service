package com.smart.drop.finance.infrastructure.persistence.jpa.adapters;

import com.smart.drop.finance.domain.model.entities.Wallet;
import com.smart.drop.finance.domain.model.entities.PaymentMethod;
import com.smart.drop.finance.domain.model.entities.WalletTransaction;
import com.smart.drop.finance.domain.model.repositories.FinanceRepository;
import com.smart.drop.finance.infrastructure.persistence.jpa.entities.WalletEntity;
import com.smart.drop.finance.infrastructure.persistence.jpa.entities.PaymentMethodEntity;
import com.smart.drop.finance.infrastructure.persistence.jpa.entities.WalletTransactionEntity;
import com.smart.drop.finance.infrastructure.persistence.jpa.repositories.WalletJpaRepository;
import com.smart.drop.finance.infrastructure.persistence.jpa.repositories.PaymentMethodJpaRepository;
import com.smart.drop.finance.infrastructure.persistence.jpa.repositories.WalletTransactionJpaRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class FinanceRepositoryAdapter implements FinanceRepository {

    private final WalletJpaRepository walletJpaRepository;
    private final PaymentMethodJpaRepository paymentMethodJpaRepository;
    private final WalletTransactionJpaRepository walletTransactionJpaRepository;

    public FinanceRepositoryAdapter(WalletJpaRepository walletJpaRepository,
                                    PaymentMethodJpaRepository paymentMethodJpaRepository,
                                    WalletTransactionJpaRepository walletTransactionJpaRepository) {
        this.walletJpaRepository = walletJpaRepository;
        this.paymentMethodJpaRepository = paymentMethodJpaRepository;
        this.walletTransactionJpaRepository = walletTransactionJpaRepository;
    }

    @Override
    public Wallet saveWallet(Wallet wallet) {
        WalletEntity entity = toEntity(wallet);
        WalletEntity saved = walletJpaRepository.save(entity);
        return toDomain(saved);
    }

    @Override
    public Optional<Wallet> findWalletByUserId(Integer userId) {
        return walletJpaRepository.findByUserId(userId).map(this::toDomain);
    }

    @Override
    public PaymentMethod savePaymentMethod(PaymentMethod paymentMethod) {
        PaymentMethodEntity entity = toEntity(paymentMethod);
        PaymentMethodEntity saved = paymentMethodJpaRepository.save(entity);
        return toDomain(saved);
    }

    @Override
    public Optional<PaymentMethod> findPaymentMethodById(Integer paymentMethodId) {
        return paymentMethodJpaRepository.findById(paymentMethodId).map(this::toDomain);
    }

    @Override
    public List<PaymentMethod> findPaymentMethodsByUserId(Integer userId) {
        return paymentMethodJpaRepository.findByUserId(userId).stream().map(this::toDomain).toList();
    }

    @Override
    public void deletePaymentMethodById(Integer paymentMethodId) {
        paymentMethodJpaRepository.deleteById(paymentMethodId);
    }

    @Override
    public WalletTransaction saveTransaction(WalletTransaction transaction) {
        WalletTransactionEntity entity = toEntity(transaction);
        WalletTransactionEntity saved = walletTransactionJpaRepository.save(entity);
        return toDomain(saved);
    }

    @Override
    public List<WalletTransaction> findTransactionsByWalletId(Integer walletId) {
        return walletTransactionJpaRepository.findByWalletIdOrderByTransactionDateDesc(walletId)
                .stream()
                .map(this::toDomain)
                .toList();
    }

    // Mappers
    private WalletEntity toEntity(Wallet wallet) {
        WalletEntity entity = new WalletEntity();
        entity.setWalletId(wallet.walletId());
        entity.setUserId(wallet.userId());
        entity.setBalance(wallet.balance());
        return entity;
    }

    private Wallet toDomain(WalletEntity entity) {
        return new Wallet(entity.getWalletId(), entity.getUserId(), entity.getBalance());
    }

    private PaymentMethodEntity toEntity(PaymentMethod method) {
        PaymentMethodEntity entity = new PaymentMethodEntity();
        entity.setPaymentMethodId(method.paymentMethodId());
        entity.setUserId(method.userId());
        entity.setType(method.type());
        entity.setLabel(method.label());
        entity.setSub(method.sub());
        entity.setPrimary(method.primary());
        return entity;
    }

    private PaymentMethod toDomain(PaymentMethodEntity entity) {
        return new PaymentMethod(
                entity.getPaymentMethodId(),
                entity.getUserId(),
                entity.getType(),
                entity.getLabel(),
                entity.getSub(),
                entity.getPrimary()
        );
    }

    private WalletTransactionEntity toEntity(WalletTransaction tx) {
        WalletTransactionEntity entity = new WalletTransactionEntity();
        entity.setTransactionId(tx.transactionId());
        entity.setWalletId(tx.walletId());
        entity.setName(tx.name());
        entity.setAmount(tx.amount());
        entity.setTransactionDate(tx.transactionDate());
        return entity;
    }

    private WalletTransaction toDomain(WalletTransactionEntity entity) {
        return new WalletTransaction(
                entity.getTransactionId(),
                entity.getWalletId(),
                entity.getName(),
                entity.getAmount(),
                entity.getTransactionDate()
        );
    }
}
