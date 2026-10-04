package com.smart.drop.finance.infrastructure.persistence.jpa.repositories;

import com.smart.drop.finance.infrastructure.persistence.jpa.entities.InvoiceEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InvoiceJpaRepository extends JpaRepository<InvoiceEntity, Integer> {

    List<InvoiceEntity> findBySubscription_SubscriptionId(Integer subscriptionId);
}

