package com.smart.drop.planning.infrastructure.external.maps;

import com.smart.drop.planning.domain.model.ports.MapsServicePort;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * Mocked adapter for Google Maps integration.
 */
@Component
public class GoogleMapsAdapter implements MapsServicePort {

    @Override
    public RouteEstimation estimate(String origin, String destination) {
        int hash = Math.abs((origin + destination).hashCode());
        BigDecimal distance = BigDecimal.valueOf((hash % 300) + 10).setScale(2);
        BigDecimal estimatedTime = distance.divide(BigDecimal.valueOf(40), 2, java.math.RoundingMode.HALF_UP);
        return new RouteEstimation(distance, estimatedTime);
    }
}

