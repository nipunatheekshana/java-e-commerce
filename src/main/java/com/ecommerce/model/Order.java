package com.ecommerce.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * A confirmed order placed by a customer. Built from the contents of a
 * {@link ShoppingCart} at checkout time.
 */
public class Order {

    private static final DateTimeFormatter FMT =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    private final String orderId;
    private final int customerId;
    private final String customerName;
    private final List<OrderItem> items = new ArrayList<>();
    private final LocalDateTime placedAt;
    private OrderStatus status;

    public Order(String orderId, Customer customer) {
        this.orderId = orderId;
        this.customerId = customer.getId();
        this.customerName = customer.getName();
        this.placedAt = LocalDateTime.now();
        this.status = OrderStatus.PLACED;
    }

    public void addItem(OrderItem item) {
        items.add(item);
    }

    public double getTotalAmount() {
        double total = 0;
        for (OrderItem item : items) {
            total += item.getSubtotal();
        }
        return total;
    }

    public boolean cancel() {
        if (status == OrderStatus.PLACED || status == OrderStatus.SHIPPED) {
            status = OrderStatus.CANCELLED;
            return true;
        }
        return false;
    }

    public String getOrderId() {
        return orderId;
    }

    public int getCustomerId() {
        return customerId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public List<OrderItem> getItems() {
        return items;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public String getPlacedAt() {
        return placedAt.format(FMT);
    }

    @Override
    public String toString() {
        return String.format("%s | %s | %-9s | %2d item(s) | $%.2f | %s",
                orderId, getPlacedAt(), status, items.size(),
                getTotalAmount(), customerName);
    }
}
