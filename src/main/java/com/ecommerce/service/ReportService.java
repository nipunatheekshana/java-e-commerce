package com.ecommerce.service;

import com.ecommerce.model.Order;
import com.ecommerce.model.OrderItem;
import com.ecommerce.model.OrderStatus;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Generates the sales / order report described in the proposal.
 * Builds the report text from the {@link OrderService} data.
 */
public class ReportService {

    private final OrderService orderService;

    public ReportService(OrderService orderService) {
        this.orderService = orderService;
    }

    public String buildSalesReport() {
        List<Order> orders = orderService.getAllOrders();
        StringBuilder sb = new StringBuilder();
        sb.append("================ SALES REPORT ================\n");

        if (orders.isEmpty()) {
            sb.append("No orders have been placed yet.\n");
            sb.append("=============================================\n");
            return sb.toString();
        }

        double grossRevenue = 0;       // every non-cancelled order
        int cancelled = 0;
        int unitsSold = 0;
        Map<String, Integer> unitsByProduct = new LinkedHashMap<>();

        for (Order order : orders) {
            if (order.getStatus() == OrderStatus.CANCELLED) {
                cancelled++;
                continue;
            }
            grossRevenue += order.getTotalAmount();
            for (OrderItem item : order.getItems()) {
                unitsSold += item.getQuantity();
                unitsByProduct.merge(item.getProductName(), item.getQuantity(), Integer::sum);
            }
        }

        int activeOrders = orders.size() - cancelled;

        sb.append(String.format("Total orders placed : %d%n", orders.size()));
        sb.append(String.format("Active orders       : %d%n", activeOrders));
        sb.append(String.format("Cancelled orders    : %d%n", cancelled));
        sb.append(String.format("Units sold          : %d%n", unitsSold));
        sb.append(String.format("Gross revenue       : $%.2f%n", grossRevenue));

        sb.append("---------------------------------------------\n");
        sb.append("Units sold by product:\n");
        if (unitsByProduct.isEmpty()) {
            sb.append("  (none)\n");
        } else {
            unitsByProduct.forEach((name, qty) ->
                    sb.append(String.format("  %-24s %d%n", name, qty)));
        }
        sb.append("=============================================\n");
        return sb.toString();
    }
}
