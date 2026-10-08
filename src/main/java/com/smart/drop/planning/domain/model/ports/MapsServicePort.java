package com.smart.drop.planning.domain.model.ports;

import java.math.BigDecimal;

public interface MapsServicePort {

    RouteEstimation estimate(String origin, String destination);

    record RouteEstimation(BigDecimal distanceKm, BigDecimal estimatedTime) {
    }
}

