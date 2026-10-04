package com.smart.drop.planning.infrastructure.persistence.jpa.repositories;

import com.smart.drop.planning.infrastructure.persistence.jpa.entities.RouteEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RouteJpaRepository extends JpaRepository<RouteEntity, Integer> {

    List<RouteEntity> findByAssignedCarrier_CarrierId(Integer carrierId);
}

