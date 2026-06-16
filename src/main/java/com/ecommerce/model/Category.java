package com.ecommerce.model;

/**
 * Product categories. Modelled as an enum so the set of categories is
 * fixed, type-safe and easy to display.
 */
public enum Category {
    ELECTRONICS,
    CLOTHING,
    BOOKS,
    GROCERY,
    HOME,
    SPORTS;

    /** Lenient lookup used by the console UI (case-insensitive). */
    public static Category fromString(String value) {
        for (Category c : values()) {
            if (c.name().equalsIgnoreCase(value.trim())) {
                return c;
            }
        }
        return null;
    }
}
