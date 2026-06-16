package com.ecommerce.ui;

import com.ecommerce.model.Admin;
import com.ecommerce.model.CartItem;
import com.ecommerce.model.Category;
import com.ecommerce.model.Customer;
import com.ecommerce.model.Order;
import com.ecommerce.model.OrderItem;
import com.ecommerce.model.Product;
import com.ecommerce.service.OrderService;
import com.ecommerce.service.ProductService;
import com.ecommerce.service.ReportService;
import com.ecommerce.service.UserService;

import java.util.List;
import java.util.Scanner;

/**
 * The "User Interface (Console)" layer. Drives the system flow described in
 * the proposal: register/login -> browse -> cart -> checkout -> order.
 */
public class ConsoleUI {

    private final Scanner in = new Scanner(System.in);
    private final UserService userService = new UserService();
    private final ProductService productService = new ProductService();
    private final OrderService orderService = new OrderService();
    private final ReportService reportService = new ReportService(orderService);

    public void run() {
        System.out.println("==================================================");
        System.out.println("     E-COMMERCE MANAGEMENT SYSTEM  (Group 03)      ");
        System.out.println("==================================================");
        System.out.println("Demo customer login : demo@shop.com / demo123");
        System.out.println("Admin login         : admin@shop.com / admin123");

        boolean running = true;
        while (running) {
            System.out.println("\n----------------- MAIN MENU -----------------");
            System.out.println("1. Customer login");
            System.out.println("2. Register new customer");
            System.out.println("3. Admin login");
            System.out.println("0. Exit");
            switch (prompt("Choose an option: ")) {
                case "1" -> customerLogin();
                case "2" -> registerCustomer();
                case "3" -> adminLogin();
                case "0" -> running = false;
                default -> System.out.println("Invalid option.");
            }
        }
        System.out.println("Goodbye!");
    }

    /* ===================== AUTH ===================== */

    private void registerCustomer() {
        System.out.println("\n--- Customer Registration ---");
        String name = prompt("Name: ");
        String email = prompt("Email: ");
        String password = prompt("Password: ");
        String phone = prompt("Phone: ");
        String address = prompt("Address: ");
        try {
            Customer customer = userService.register(name, email, password, phone, address);
            System.out.println("Registered successfully. Welcome, " + customer.getName() + "!");
            customerMenu(customer);
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void customerLogin() {
        System.out.println("\n--- Customer Login ---");
        String email = prompt("Email: ");
        String password = prompt("Password: ");
        Customer customer = userService.loginCustomer(email, password);
        if (customer == null) {
            System.out.println("Invalid credentials.");
        } else {
            System.out.println("Welcome back, " + customer.getName() + "!");
            customerMenu(customer);
        }
    }

    private void adminLogin() {
        System.out.println("\n--- Admin Login ---");
        String email = prompt("Email: ");
        String password = prompt("Password: ");
        Admin admin = userService.loginAdmin(email, password);
        if (admin == null) {
            System.out.println("Invalid credentials.");
        } else {
            System.out.println("Welcome, " + admin.getName() + ".");
            adminMenu();
        }
    }

    /* ===================== CUSTOMER ===================== */

    private void customerMenu(Customer customer) {
        boolean active = true;
        while (active) {
            System.out.println("\n--------------- CUSTOMER MENU ---------------");
            System.out.println("1. Browse all products");
            System.out.println("2. Search products");
            System.out.println("3. Browse by category");
            System.out.println("4. Add product to cart");
            System.out.println("5. View cart");
            System.out.println("6. Update cart quantity");
            System.out.println("7. Remove from cart");
            System.out.println("8. Checkout / place order");
            System.out.println("9. View order history");
            System.out.println("10. Cancel an order");
            System.out.println("11. Update my profile");
            System.out.println("0. Logout");
            switch (prompt("Choose an option: ")) {
                case "1" -> printProducts(productService.getAll());
                case "2" -> searchProducts();
                case "3" -> browseByCategory();
                case "4" -> addToCart(customer);
                case "5" -> viewCart(customer);
                case "6" -> updateCart(customer);
                case "7" -> removeFromCart(customer);
                case "8" -> checkout(customer);
                case "9" -> viewOrderHistory(customer);
                case "10" -> cancelOrder(customer);
                case "11" -> updateProfile(customer);
                case "0" -> active = false;
                default -> System.out.println("Invalid option.");
            }
        }
    }

    private void searchProducts() {
        String keyword = prompt("Search keyword: ");
        printProducts(productService.search(keyword));
    }

    private void browseByCategory() {
        System.out.println("Categories: " + java.util.Arrays.toString(Category.values()));
        Category category = Category.fromString(prompt("Category: "));
        if (category == null) {
            System.out.println("Unknown category.");
            return;
        }
        printProducts(productService.byCategory(category));
    }

    private void addToCart(Customer customer) {
        printProducts(productService.getAll());
        Integer id = promptInt("Product id to add: ");
        if (id == null) return;
        Product product = productService.findById(id);
        if (product == null) {
            System.out.println("No such product.");
            return;
        }
        Integer qty = promptInt("Quantity: ");
        if (qty == null) return;
        if (!product.isInStock(qty)) {
            System.out.println("Only " + product.getStock() + " in stock.");
            return;
        }
        customer.getCart().addProduct(product, qty);
        System.out.println("Added " + qty + " x " + product.getName() + " to cart.");
    }

    private void viewCart(Customer customer) {
        List<CartItem> items = customer.getCart().getItems();
        System.out.println("\n--- Your Cart ---");
        if (items.isEmpty()) {
            System.out.println("(empty)");
            return;
        }
        for (CartItem item : items) {
            System.out.println("  " + item);
        }
        System.out.printf("Total: $%.2f%n", customer.getCart().getTotal());
    }

    private void updateCart(Customer customer) {
        viewCart(customer);
        if (customer.getCart().isEmpty()) return;
        Integer id = promptInt("Product id to update: ");
        if (id == null) return;
        Integer qty = promptInt("New quantity (0 to remove): ");
        if (qty == null) return;
        if (customer.getCart().updateQuantity(id, qty)) {
            System.out.println("Cart updated.");
        } else {
            System.out.println("Product not in cart.");
        }
    }

    private void removeFromCart(Customer customer) {
        viewCart(customer);
        if (customer.getCart().isEmpty()) return;
        Integer id = promptInt("Product id to remove: ");
        if (id == null) return;
        if (customer.getCart().removeProduct(id)) {
            System.out.println("Removed.");
        } else {
            System.out.println("Product not in cart.");
        }
    }

    private void checkout(Customer customer) {
        viewCart(customer);
        if (customer.getCart().isEmpty()) return;
        if (!prompt("Confirm order? (y/n): ").equalsIgnoreCase("y")) {
            System.out.println("Checkout cancelled.");
            return;
        }
        try {
            Order order = orderService.placeOrder(customer);
            System.out.println("\nOrder placed successfully!");
            printOrder(order);
        } catch (RuntimeException e) {
            System.out.println("Could not place order: " + e.getMessage());
        }
    }

    private void viewOrderHistory(Customer customer) {
        System.out.println("\n--- Order History ---");
        if (customer.getOrderHistory().isEmpty()) {
            System.out.println("(no orders yet)");
            return;
        }
        for (Order order : customer.getOrderHistory()) {
            System.out.println("  " + order);
        }
    }

    private void cancelOrder(Customer customer) {
        viewOrderHistory(customer);
        if (customer.getOrderHistory().isEmpty()) return;
        String id = prompt("Order id to cancel: ");
        Order order = orderService.findById(id);
        if (order == null || order.getCustomerId() != customer.getId()) {
            System.out.println("Order not found.");
            return;
        }
        if (order.cancel()) {
            System.out.println("Order " + id + " cancelled.");
        } else {
            System.out.println("Order cannot be cancelled (status: " + order.getStatus() + ").");
        }
    }

    private void updateProfile(Customer customer) {
        System.out.println("\n--- Update Profile (leave blank to keep) ---");
        String name = prompt("Name [" + customer.getName() + "]: ");
        if (!name.isBlank()) customer.setName(name);
        String phone = prompt("Phone [" + customer.getPhone() + "]: ");
        if (!phone.isBlank()) customer.setPhone(phone);
        String address = prompt("Address [" + customer.getAddress() + "]: ");
        if (!address.isBlank()) customer.setAddress(address);
        System.out.println("Profile updated.");
    }

    /* ===================== ADMIN ===================== */

    private void adminMenu() {
        boolean active = true;
        while (active) {
            System.out.println("\n---------------- ADMIN MENU ----------------");
            System.out.println("1. View all products");
            System.out.println("2. Add product");
            System.out.println("3. Update product");
            System.out.println("4. Delete product");
            System.out.println("5. View all customers");
            System.out.println("6. View all orders");
            System.out.println("7. Generate sales report");
            System.out.println("0. Logout");
            switch (prompt("Choose an option: ")) {
                case "1" -> printProducts(productService.getAll());
                case "2" -> addProduct();
                case "3" -> updateProduct();
                case "4" -> deleteProduct();
                case "5" -> viewCustomers();
                case "6" -> viewAllOrders();
                case "7" -> System.out.println("\n" + reportService.buildSalesReport());
                case "0" -> active = false;
                default -> System.out.println("Invalid option.");
            }
        }
    }

    private void addProduct() {
        System.out.println("\n--- Add Product ---");
        String name = prompt("Name: ");
        System.out.println("Categories: " + java.util.Arrays.toString(Category.values()));
        Category category = Category.fromString(prompt("Category: "));
        if (category == null) {
            System.out.println("Unknown category.");
            return;
        }
        Double price = promptDouble("Price: ");
        if (price == null) return;
        Integer stock = promptInt("Stock: ");
        if (stock == null) return;
        String description = prompt("Description: ");
        Product product = productService.add(name, category, price, stock, description);
        System.out.println("Added: " + product);
    }

    private void updateProduct() {
        printProducts(productService.getAll());
        Integer id = promptInt("Product id to update: ");
        if (id == null) return;
        if (productService.findById(id) == null) {
            System.out.println("No such product.");
            return;
        }
        String name = prompt("New name (blank = keep): ");
        Double price = promptDoubleOptional("New price (blank = keep): ");
        Integer stock = promptIntOptional("New stock (blank = keep): ");
        productService.update(id, name, price, stock);
        System.out.println("Updated: " + productService.findById(id));
    }

    private void deleteProduct() {
        printProducts(productService.getAll());
        Integer id = promptInt("Product id to delete: ");
        if (id == null) return;
        if (productService.delete(id)) {
            System.out.println("Deleted.");
        } else {
            System.out.println("No such product.");
        }
    }

    private void viewCustomers() {
        System.out.println("\n--- Customers ---");
        List<Customer> customers = userService.getAllCustomers();
        if (customers.isEmpty()) {
            System.out.println("(none)");
            return;
        }
        for (Customer customer : customers) {
            System.out.printf("  %s | phone: %s | orders: %d%n",
                    customer, customer.getPhone(), customer.getOrderHistory().size());
        }
    }

    private void viewAllOrders() {
        System.out.println("\n--- All Orders ---");
        List<Order> orders = orderService.getAllOrders();
        if (orders.isEmpty()) {
            System.out.println("(none)");
            return;
        }
        for (Order order : orders) {
            System.out.println("  " + order);
        }
    }

    /* ===================== HELPERS ===================== */

    private void printProducts(List<Product> products) {
        System.out.println("\n--- Products ---");
        if (products.isEmpty()) {
            System.out.println("(no products)");
            return;
        }
        for (Product product : products) {
            System.out.println("  " + product);
        }
    }

    private void printOrder(Order order) {
        System.out.println("Order " + order.getOrderId() + "  (" + order.getStatus() + ")");
        for (OrderItem item : order.getItems()) {
            System.out.println("  " + item);
        }
        System.out.printf("Total: $%.2f%n", order.getTotalAmount());
    }

    private String prompt(String message) {
        System.out.print(message);
        if (!in.hasNextLine()) {
            return "0"; // EOF (e.g. piped input ended) -> behave like exit
        }
        return in.nextLine().trim();
    }

    private Integer promptInt(String message) {
        String raw = prompt(message);
        try {
            return Integer.parseInt(raw);
        } catch (NumberFormatException e) {
            System.out.println("Please enter a whole number.");
            return null;
        }
    }

    private Integer promptIntOptional(String message) {
        String raw = prompt(message);
        if (raw.isBlank()) return null;
        try {
            return Integer.parseInt(raw);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private Double promptDouble(String message) {
        String raw = prompt(message);
        try {
            return Double.parseDouble(raw);
        } catch (NumberFormatException e) {
            System.out.println("Please enter a valid amount.");
            return null;
        }
    }

    private Double promptDoubleOptional(String message) {
        String raw = prompt(message);
        if (raw.isBlank()) return null;
        try {
            return Double.parseDouble(raw);
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
