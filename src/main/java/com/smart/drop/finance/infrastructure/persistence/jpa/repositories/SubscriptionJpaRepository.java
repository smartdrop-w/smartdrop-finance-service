package com.smart.drop.finance.infrastructure.persistence.jpa.repositories;

import com.smart.drop.finance.infrastructure.persistence.jpa.entities.SubscriptionEntity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SubscriptionJpaRepository extends JpaRepository<SubscriptionEntity, Integer> {

    @EntityGraph(attributePaths = "plan")
    List<SubscriptionEntity> findByUserId(Integer userId);
}

