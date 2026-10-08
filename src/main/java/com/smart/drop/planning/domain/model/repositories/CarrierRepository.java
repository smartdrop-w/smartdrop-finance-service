package com.smart.drop.planning.domain.model.repositories;

import com.smart.drop.planning.domain.model.entities.Carrier;

import java.util.List;
import java.util.Optional;

public interface CarrierRepository {

    Carrier save(Carrier carrier);

    Optional<Carrier> findById(Integer carrierId);

    List<Carrier> findAll();
}

