package com.ecommerce.service;

import com.ecommerce.model.CartItem;
import com.ecommerce.model.Customer;
import com.ecommerce.model.Order;
import com.ecommerce.model.OrderItem;
import com.ecommerce.model.Product;
import com.ecommerce.model.ShoppingCart;

import java.util.ArrayList;
import java.util.List;

/**
 * The "Order Module": turns a cart into an order, generates order IDs,
 * reduces stock, and supports tracking and cancellation.
 */
public class OrderService {

    private final List<Order> orders = new ArrayList<>();
    private int sequence = 1000;

    /**
     * Places an order for the customer's current cart. Validates stock,
     * reduces it, records the order, clears the cart and returns the order.
     *
     * @throws IllegalStateException    if the cart is empty
     * @throws IllegalArgumentException if any line exceeds available stock
     */
    public Order placeOrder(Customer customer) {
        ShoppingCart cart = customer.getCart();
        if (cart.isEmpty()) {
            throw new IllegalStateException("Cart is empty.");
        }

        // Validate stock for the whole cart before committing anything.
        for (CartItem item : cart.getItems()) {
            Product product = item.getProduct();
            if (!product.isInStock(item.getQuantity())) {
                throw new IllegalArgumentException(
                        "Insufficient stock for " + product.getName()
                                + " (have " + product.getStock()
                                + ", need " + item.getQuantity() + ")");
            }
        }

        Order order = new Order(generateOrderId(), customer);
        for (CartItem item : cart.getItems()) {
            Product product = item.getProduct();
            product.reduceStock(item.getQuantity());
            order.addItem(new OrderItem(product, item.getQuantity()));
        }

        orders.add(order);
        customer.addOrder(order);
        cart.clear();
        return order;
    }

    public Order findById(String orderId) {
        for (Order order : orders) {
            if (order.getOrderId().equalsIgnoreCase(orderId)) {
                return order;
            }
        }
        return null;
    }

    public boolean cancelOrder(String orderId) {
        Order order = findById(orderId);
        return order != null && order.cancel();
    }

    public List<Order> getAllOrders() {
        return new ArrayList<>(orders);
    }

    private String generateOrderId() {
        return "ORD-" + (++sequence);
    }
}
