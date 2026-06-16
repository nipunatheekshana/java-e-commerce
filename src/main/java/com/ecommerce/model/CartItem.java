package com.ecommerce.model;

/**
 * A single line in a {@link ShoppingCart}: a product and how many of it.
 */
public class CartItem {

    private final Product product;
    private int quantity;

    public CartItem(Product product, int quantity) {
        this.product = product;
        this.quantity = quantity;
    }

    public double getSubtotal() {
        return product.getPrice() * quantity;
    }

    public Product getProduct() {
        return product;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return String.format("%-22s  x%-3d  $%8.2f",
                product.getName(), quantity, getSubtotal());
    }
}
