package com.ecommerce.model;

/**
 * Abstract base class for every system user.
 * Demonstrates ABSTRACTION and ENCAPSULATION: fields are private and shared
 * behaviour lives here, while each concrete user defines its own role.
 */
public abstract class User {

    private final int id;
    private String name;
    private String email;
    private String password;

    protected User(int id, String name, String email, String password) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
    }

    /** Each subclass reports its role — demonstrates POLYMORPHISM. */
    public abstract String getRole();

    public boolean checkPassword(String candidate) {
        return password.equals(candidate);
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return String.format("#%d %s <%s> [%s]", id, name, email, getRole());
    }
}
