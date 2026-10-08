package com.smart.drop.planning.application.services;

import com.smart.drop.planning.domain.model.entities.Carrier;
import com.smart.drop.planning.domain.model.entities.Route;
import com.smart.drop.planning.domain.model.repositories.CarrierRepository;
import com.smart.drop.planning.domain.model.repositories.RouteRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class AssignmentService {

    private final RouteRepository routeRepository;
    private final CarrierRepository carrierRepository;

    public AssignmentService(RouteRepository routeRepository, CarrierRepository carrierRepository) {
        this.routeRepository = routeRepository;
        this.carrierRepository = carrierRepository;
    }

    public Carrier createCarrier(String name, String contactPhone) {
        Carrier carrier = Carrier.create(name, contactPhone);
        return carrierRepository.save(carrier);
    }

    public Route assignCarrierToRoute(Integer routeId, Integer carrierId) {
        Route route = routeRepository.findById(routeId)
                .orElseThrow(() -> new IllegalArgumentException("Route not found: " + routeId));

        carrierRepository.findById(carrierId)
                .orElseThrow(() -> new IllegalArgumentException("Carrier not found: " + carrierId));

        Route updated = route.assignCarrier(carrierId);
        return routeRepository.update(updated);
    }

    @Transactional(readOnly = true)
    public Optional<Carrier> getCarrierById(Integer carrierId) {
        return carrierRepository.findById(carrierId);
    }

    @Transactional(readOnly = true)
    public List<Carrier> getAllCarriers() {
        return carrierRepository.findAll();
    }
}

