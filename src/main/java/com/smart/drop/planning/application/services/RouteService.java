package com.smart.drop.planning.application.services;

import com.smart.drop.planning.domain.model.entities.Route;
import com.smart.drop.planning.domain.model.ports.MapsServicePort;
import com.smart.drop.planning.domain.model.repositories.RouteRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class RouteService {

    private final RouteRepository routeRepository;
    private final MapsServicePort mapsServicePort;

    public RouteService(RouteRepository routeRepository, MapsServicePort mapsServicePort) {
        this.routeRepository = routeRepository;
        this.mapsServicePort = mapsServicePort;
    }

    public Route calculateAndCreateRoute(String origin, String destination, Integer assignedCarrierId) {
        MapsServicePort.RouteEstimation estimation = mapsServicePort.estimate(origin, destination);
        Route route = Route.create(origin, destination, estimation.distanceKm(), estimation.estimatedTime(), assignedCarrierId);
        return routeRepository.save(route);
    }

    @Transactional(readOnly = true)
    public Optional<Route> getById(Integer routeId) {
        return routeRepository.findById(routeId);
    }

    @Transactional(readOnly = true)
    public List<Route> getAll() {
        return routeRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<Route> getByCarrier(Integer carrierId) {
        return routeRepository.findByAssignedCarrierId(carrierId);
    }
}

