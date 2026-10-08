package com.smart.drop.planning.infrastructure.persistence.jpa.repositories;

import com.smart.drop.planning.infrastructure.persistence.jpa.entities.CarrierEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CarrierJpaRepository extends JpaRepository<CarrierEntity, Integer> {
}

