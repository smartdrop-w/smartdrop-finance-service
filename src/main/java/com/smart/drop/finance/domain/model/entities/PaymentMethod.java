package com.smart.drop.finance.domain.model.entities;

/**
 * Immutable domain entity representing a user's payment method.
 */
public record PaymentMethod(
        Integer paymentMethodId,
        Integer userId,
        String type,
        String label,
        String sub,
        Boolean primary
) {
    public PaymentMethod {
        if (userId == null) {
            throw new IllegalArgumentException("UserId cannot be null");
        }
        if (label == null || label.isBlank()) {
            throw new IllegalArgumentException("Label cannot be null or blank");
        }
    }

    public static PaymentMethod create(Integer userId, String type, String label, String sub, Boolean primary) {
        return new PaymentMethod(null, userId, type, label, sub, primary);
    }
}
