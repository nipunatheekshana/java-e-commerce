package com.ecommerce.service;

import com.ecommerce.model.Admin;
import com.ecommerce.model.Customer;
import com.ecommerce.model.User;

import java.util.ArrayList;
import java.util.List;

/**
 * Handles registration, authentication and the in-memory user store
 * (the "Customer Module" + "Admin Module" from the proposal).
 */
public class UserService {

    private final List<Customer> customers = new ArrayList<>();
    private final List<Admin> admins = new ArrayList<>();
    private int nextCustomerId = 1;
    private int nextAdminId = 1;

    public UserService() {
        // Default administrator account.
        admins.add(new Admin(nextAdminId++, "Store Admin", "admin@shop.com", "admin123"));
        // A demo customer so the app is usable immediately.
        register("Demo Customer", "demo@shop.com", "demo123", "0771234567", "Colombo");
    }

    public Customer register(String name, String email, String password,
                             String phone, String address) {
        if (findCustomerByEmail(email) != null) {
            throw new IllegalArgumentException("Email already registered: " + email);
        }
        Customer customer = new Customer(nextCustomerId++, name, email, password, phone, address);
        customers.add(customer);
        return customer;
    }

    public Customer loginCustomer(String email, String password) {
        Customer customer = findCustomerByEmail(email);
        if (customer != null && customer.checkPassword(password)) {
            return customer;
        }
        return null;
    }

    public Admin loginAdmin(String email, String password) {
        for (Admin admin : admins) {
            if (admin.getEmail().equalsIgnoreCase(email) && admin.checkPassword(password)) {
                return admin;
            }
        }
        return null;
    }

    public Customer findCustomerByEmail(String email) {
        for (Customer customer : customers) {
            if (customer.getEmail().equalsIgnoreCase(email)) {
                return customer;
            }
        }
        return null;
    }

    public List<Customer> getAllCustomers() {
        return new ArrayList<>(customers);
    }

    public List<User> getAllUsers() {
        List<User> all = new ArrayList<>();
        all.addAll(admins);
        all.addAll(customers);
        return all;
    }
}
