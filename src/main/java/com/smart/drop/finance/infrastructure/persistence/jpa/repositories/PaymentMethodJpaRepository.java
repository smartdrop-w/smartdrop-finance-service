package com.smart.drop.finance.infrastructure.persistence.jpa.repositories;

import com.smart.drop.finance.infrastructure.persistence.jpa.entities.PaymentMethodEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PaymentMethodJpaRepository extends JpaRepository<PaymentMethodEntity, Integer> {

    List<PaymentMethodEntity> findByUserId(Integer userId);
}
