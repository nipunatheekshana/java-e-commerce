package com.ecommerce.service;

import com.ecommerce.model.Category;
import com.ecommerce.model.Product;

import java.util.ArrayList;
import java.util.List;

/**
 * In-memory product catalogue. Acts as the "Product Module" and part of the
 * "Data Storage Layer" from the proposal. Swapping this for a database-backed
 * implementation would not affect the rest of the system.
 */
public class ProductService {

    private final List<Product> products = new ArrayList<>();
    private int nextId = 1;

    public ProductService() {
        seed();
    }

    private void seed() {
        add("Laptop", Category.ELECTRONICS, 950.00, 15, "14-inch, 16GB RAM");
        add("Smartphone", Category.ELECTRONICS, 499.99, 30, "6.1-inch OLED");
        add("Headphones", Category.ELECTRONICS, 79.50, 50, "Wireless, noise cancelling");
        add("T-Shirt", Category.CLOTHING, 12.00, 100, "100% cotton");
        add("Jeans", Category.CLOTHING, 39.99, 60, "Slim fit");
        add("Java Programming", Category.BOOKS, 45.00, 40, "OOP fundamentals");
        add("Coffee Beans 1kg", Category.GROCERY, 18.25, 80, "Arabica roast");
        add("Desk Lamp", Category.HOME, 24.99, 25, "LED, adjustable");
        add("Football", Category.SPORTS, 29.00, 35, "Size 5, match ball");
    }

    public Product add(String name, Category category, double price,
                       int stock, String description) {
        Product product = new Product(nextId++, name, category, price, stock, description);
        products.add(product);
        return product;
    }

    public Product findById(int id) {
        for (Product product : products) {
            if (product.getId() == id) {
                return product;
            }
        }
        return null;
    }

    public boolean update(int id, String name, Double price, Integer stock) {
        Product product = findById(id);
        if (product == null) {
            return false;
        }
        if (name != null && !name.isBlank()) {
            product.setName(name);
        }
        if (price != null) {
            product.setPrice(price);
        }
        if (stock != null) {
            product.setStock(stock);
        }
        return true;
    }

    public boolean delete(int id) {
        return products.removeIf(p -> p.getId() == id);
    }

    public List<Product> getAll() {
        return new ArrayList<>(products);
    }

    public List<Product> search(String keyword) {
        String needle = keyword.toLowerCase();
        List<Product> matches = new ArrayList<>();
        for (Product product : products) {
            if (product.getName().toLowerCase().contains(needle)) {
                matches.add(product);
            }
        }
        return matches;
    }

    public List<Product> byCategory(Category category) {
        List<Product> matches = new ArrayList<>();
        for (Product product : products) {
            if (product.getCategory() == category) {
                matches.add(product);
            }
        }
        return matches;
    }
}
