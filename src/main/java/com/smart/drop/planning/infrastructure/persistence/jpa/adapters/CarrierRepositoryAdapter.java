package com.smart.drop.planning.infrastructure.persistence.jpa.adapters;

import com.smart.drop.planning.domain.model.entities.Carrier;
import com.smart.drop.planning.domain.model.repositories.CarrierRepository;
import com.smart.drop.planning.infrastructure.persistence.jpa.entities.CarrierEntity;
import com.smart.drop.planning.infrastructure.persistence.jpa.repositories.CarrierJpaRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class CarrierRepositoryAdapter implements CarrierRepository {

    private final CarrierJpaRepository carrierJpaRepository;

    public CarrierRepositoryAdapter(CarrierJpaRepository carrierJpaRepository) {
        this.carrierJpaRepository = carrierJpaRepository;
    }

    @Override
    public Carrier save(Carrier carrier) {
        CarrierEntity entity = new CarrierEntity();
        entity.setCarrierId(carrier.carrierId());
        entity.setName(carrier.name());
        entity.setContactPhone(carrier.contactPhone());
        entity.setStatus(carrier.status());
        CarrierEntity saved = carrierJpaRepository.save(entity);
        return toDomain(saved);
    }

    @Override
    public Optional<Carrier> findById(Integer carrierId) {
        return carrierJpaRepository.findById(carrierId).map(this::toDomain);
    }

    @Override
    public List<Carrier> findAll() {
        return carrierJpaRepository.findAll().stream().map(this::toDomain).toList();
    }

    private Carrier toDomain(CarrierEntity entity) {
        return new Carrier(entity.getCarrierId(), entity.getName(), entity.getContactPhone(), entity.getStatus());
    }
}

