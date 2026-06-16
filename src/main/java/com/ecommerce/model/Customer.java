package com.ecommerce.model;

import java.util.ArrayList;
import java.util.List;

/**
 * A customer of the store. Owns a shopping cart and an order history.
 * Demonstrates INHERITANCE (extends {@link User}).
 */
public class Customer extends User {

    private String phone;
    private String address;
    private final ShoppingCart cart = new ShoppingCart();
    private final List<Order> orderHistory = new ArrayList<>();

    public Customer(int id, String name, String email, String password,
                    String phone, String address) {
        super(id, name, email, password);
        this.phone = phone;
        this.address = address;
    }

    @Override
    public String getRole() {
        return "CUSTOMER";
    }

    public ShoppingCart getCart() {
        return cart;
    }

    public List<Order> getOrderHistory() {
        return orderHistory;
    }

    public void addOrder(Order order) {
        orderHistory.add(order);
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
