package com.ecommerce.model;

/**
 * A snapshot of one line in a placed {@link Order}. The name and price are
 * copied at purchase time so later product edits do not change past orders.
 */
public class OrderItem {

    private final int productId;
    private final String productName;
    private final double unitPrice;
    private final int quantity;

    public OrderItem(Product product, int quantity) {
        this.productId = product.getId();
        this.productName = product.getName();
        this.unitPrice = product.getPrice();
        this.quantity = quantity;
    }

    public double getSubtotal() {
        return unitPrice * quantity;
    }

    public int getProductId() {
        return productId;
    }

    public String getProductName() {
        return productName;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public int getQuantity() {
        return quantity;
    }

    @Override
    public String toString() {
        return String.format("%-22s  x%-3d  @ $%.2f  = $%.2f",
                productName, quantity, unitPrice, getSubtotal());
    }
}
