package com.ecommerce.model;

/**
 * The lifecycle states an {@link Order} can be in.
 */
public enum OrderStatus {
    PLACED,
    SHIPPED,
    DELIVERED,
    CANCELLED
}
