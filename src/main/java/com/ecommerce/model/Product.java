package com.ecommerce.model;

/**
 * A product offered in the store. Stock is tracked here so the order
 * module can reduce it when an order is placed.
 */
public class Product {

    private final int id;
    private String name;
    private Category category;
    private double price;
    private int stock;
    private String description;

    public Product(int id, String name, Category category, double price,
                   int stock, String description) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.price = price;
        this.stock = stock;
        this.description = description;
    }

    public boolean isInStock(int quantity) {
        return stock >= quantity && quantity > 0;
    }

    public void reduceStock(int quantity) {
        if (!isInStock(quantity)) {
            throw new IllegalArgumentException("Not enough stock for " + name);
        }
        stock -= quantity;
    }

    public void increaseStock(int quantity) {
        stock += quantity;
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

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return String.format("#%-3d %-22s %-12s $%8.2f  (stock: %d)",
                id, name, category, price, stock);
    }
}
