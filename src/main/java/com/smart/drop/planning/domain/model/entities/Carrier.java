package com.smart.drop.planning.domain.model.entities;

/**
 * Immutable domain entity for service carriers.
 */
public record Carrier(
        Integer carrierId,
        String name,
        String contactPhone,
        String status
) {
    public Carrier {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("name is required");
        }
        if (name.length() > 100) {
            throw new IllegalArgumentException("name must be <= 100 characters");
        }
        if (contactPhone != null && contactPhone.length() > 20) {
            throw new IllegalArgumentException("contactPhone must be <= 20 characters");
        }
        if (status != null && status.length() > 20) {
            throw new IllegalArgumentException("status must be <= 20 characters");
        }
    }

    public static Carrier create(String name, String contactPhone) {
        return new Carrier(null, name, contactPhone, "ACTIVE");
    }
}

