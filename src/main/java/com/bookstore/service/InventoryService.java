package com.bookstore.service;

import com.bookstore.model.Book;
import com.bookstore.model.OrderItem;

import java.util.List;

public interface InventoryService {

    /**
     * Reduce stock for the given order items
     * @param items List of OrderItem to reduce stock for
     */
    void reduceStock(List<OrderItem> items);

    /**
     * Restore stock for the given order items (e.g., when order is canceled)
     * @param items List of OrderItem to restore stock
     */
    void restoreStock(List<OrderItem> items);

    /**
     * Check if stock is sufficient for the given order items
     * @param items List of OrderItem to check
     * @throws RuntimeException if any book has insufficient stock
     */
    void checkStock(List<OrderItem> items);
}
