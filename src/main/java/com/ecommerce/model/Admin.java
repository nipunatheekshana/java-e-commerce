package com.ecommerce.model;

/**
 * A system administrator. Demonstrates INHERITANCE and POLYMORPHISM:
 * shares the {@link User} contract but reports a different role.
 */
public class Admin extends User {

    public Admin(int id, String name, String email, String password) {
        super(id, name, email, password);
    }

    @Override
    public String getRole() {
        return "ADMIN";
    }
}
