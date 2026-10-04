package com.smart.drop.finance.infrastructure.persistence.jpa.repositories;

import com.smart.drop.finance.infrastructure.persistence.jpa.entities.WalletTransactionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WalletTransactionJpaRepository extends JpaRepository<WalletTransactionEntity, Integer> {

    List<WalletTransactionEntity> findByWalletIdOrderByTransactionDateDesc(Integer walletId);
}
