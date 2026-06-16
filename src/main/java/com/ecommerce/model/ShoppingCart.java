package com.ecommerce.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Holds the items a customer intends to buy. Adding the same product twice
 * merges the quantities rather than creating a duplicate line.
 */
public class ShoppingCart {

    private final List<CartItem> items = new ArrayList<>();

    public void addProduct(Product product, int quantity) {
        for (CartItem item : items) {
            if (item.getProduct().getId() == product.getId()) {
                item.setQuantity(item.getQuantity() + quantity);
                return;
            }
        }
        items.add(new CartItem(product, quantity));
    }

    public boolean removeProduct(int productId) {
        return items.removeIf(item -> item.getProduct().getId() == productId);
    }

    public boolean updateQuantity(int productId, int quantity) {
        for (CartItem item : items) {
            if (item.getProduct().getId() == productId) {
                if (quantity <= 0) {
                    items.remove(item);
                } else {
                    item.setQuantity(quantity);
                }
                return true;
            }
        }
        return false;
    }

    public double getTotal() {
        double total = 0;
        for (CartItem item : items) {
            total += item.getSubtotal();
        }
        return total;
    }

    public List<CartItem> getItems() {
        return items;
    }

    public boolean isEmpty() {
        return items.isEmpty();
    }

    public void clear() {
        items.clear();
    }
}
