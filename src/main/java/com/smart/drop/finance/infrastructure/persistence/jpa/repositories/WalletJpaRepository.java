package com.smart.drop.finance.infrastructure.persistence.jpa.repositories;

import com.smart.drop.finance.infrastructure.persistence.jpa.entities.WalletEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface WalletJpaRepository extends JpaRepository<WalletEntity, Integer> {

    Optional<WalletEntity> findByUserId(Integer userId);
}
