package com.smart.drop.planning.domain.model.repositories;

import com.smart.drop.planning.domain.model.entities.Route;

import java.util.List;
import java.util.Optional;

public interface RouteRepository {

    Route save(Route route);

    Route update(Route route);

    Optional<Route> findById(Integer routeId);

    List<Route> findAll();

    List<Route> findByAssignedCarrierId(Integer carrierId);
}

