package com.smart.drop.planning.interfaces.rest.dto;

public record CarrierResponse(
        Integer carrierId,
        String name,
        String contactPhone,
        String status
) {
}

