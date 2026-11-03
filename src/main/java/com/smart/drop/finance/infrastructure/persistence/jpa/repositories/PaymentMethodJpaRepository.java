package com.smart.drop.finance.infrastructure.persistence.jpa.repositories;

import com.smart.drop.finance.infrastructure.persistence.jpa.entities.PaymentMethodEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PaymentMethodJpaRepository extends JpaRepository<PaymentMethodEntity, Integer> {

    List<PaymentMethodEntity> findByUserId(Integer userId);

    @Modifying
    @Query("UPDATE PaymentMethodEntity p SET p.primary = false WHERE p.userId = :userId")
    void resetPrimaryForUser(@Param("userId") Integer userId);

    @Modifying
    @Query("UPDATE PaymentMethodEntity p SET p.primary = CASE WHEN p.paymentMethodId = :targetId THEN true ELSE false END WHERE p.userId = :userId")
    void setPrimaryPaymentMethod(@Param("userId") Integer userId, @Param("targetId") Integer targetId);
}
