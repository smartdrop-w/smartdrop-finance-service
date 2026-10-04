package com.smart.drop.planning.infrastructure.persistence.jpa.adapters;

import com.smart.drop.planning.domain.model.entities.Route;
import com.smart.drop.planning.domain.model.repositories.RouteRepository;
import com.smart.drop.planning.infrastructure.persistence.jpa.entities.CarrierEntity;
import com.smart.drop.planning.infrastructure.persistence.jpa.entities.RouteEntity;
import com.smart.drop.planning.infrastructure.persistence.jpa.repositories.CarrierJpaRepository;
import com.smart.drop.planning.infrastructure.persistence.jpa.repositories.RouteJpaRepository;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Component
public class RouteRepositoryAdapter implements RouteRepository {

    private final RouteJpaRepository routeJpaRepository;
    private final CarrierJpaRepository carrierJpaRepository;

    public RouteRepositoryAdapter(RouteJpaRepository routeJpaRepository, CarrierJpaRepository carrierJpaRepository) {
        this.routeJpaRepository = routeJpaRepository;
        this.carrierJpaRepository = carrierJpaRepository;
    }

    @Override
    public Route save(Route route) {
        RouteEntity entity = toEntity(route);
        if (entity.getCreatedAt() == null) {
            entity.setCreatedAt(LocalDateTime.now());
        }
        RouteEntity saved = routeJpaRepository.save(entity);
        return toDomain(saved);
    }

    @Override
    public Route update(Route route) {
        RouteEntity existing = routeJpaRepository.findById(route.routeId())
                .orElseThrow(() -> new IllegalArgumentException("Route not found: " + route.routeId()));

        existing.setOrigin(route.origin());
        existing.setDestination(route.destination());
        existing.setDistanceKm(route.distanceKm());
        existing.setEstimatedTime(route.estimatedTime());
        existing.setAssignedCarrier(resolveCarrier(route.assignedCarrierId()));

        RouteEntity updated = routeJpaRepository.save(existing);
        return toDomain(updated);
    }

    @Override
    public Optional<Route> findById(Integer routeId) {
        return routeJpaRepository.findById(routeId).map(this::toDomain);
    }

    @Override
    public List<Route> findAll() {
        return routeJpaRepository.findAll().stream().map(this::toDomain).toList();
    }

    @Override
    public List<Route> findByAssignedCarrierId(Integer carrierId) {
        return routeJpaRepository.findByAssignedCarrier_CarrierId(carrierId).stream().map(this::toDomain).toList();
    }

    private RouteEntity toEntity(Route route) {
        RouteEntity entity = new RouteEntity();
        entity.setRouteId(route.routeId());
        entity.setOrigin(route.origin());
        entity.setDestination(route.destination());
        entity.setDistanceKm(route.distanceKm());
        entity.setEstimatedTime(route.estimatedTime());
        entity.setAssignedCarrier(resolveCarrier(route.assignedCarrierId()));
        entity.setCreatedAt(route.createdAt());
        return entity;
    }

    private CarrierEntity resolveCarrier(Integer carrierId) {
        if (carrierId == null) {
            return null;
        }
        return carrierJpaRepository.findById(carrierId)
                .orElseThrow(() -> new IllegalArgumentException("Carrier not found: " + carrierId));
    }

    private Route toDomain(RouteEntity entity) {
        return new Route(
                entity.getRouteId(),
                entity.getOrigin(),
                entity.getDestination(),
                entity.getDistanceKm(),
                entity.getEstimatedTime(),
                entity.getAssignedCarrier() == null ? null : entity.getAssignedCarrier().getCarrierId(),
                entity.getCreatedAt()
        );
    }
}

