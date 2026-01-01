package com.bookstore.repository;

import com.bookstore.model.Order;
import com.bookstore.model.OrderItem;
import com.bookstore.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

    // Find all items for a specific order
    List<OrderItem> findByOrder(Order order);

    // Find all items for a specific book
    List<OrderItem> findByBook(Book book);

    // Find a specific item by order and book (useful for checking if a book is already in an order)
    Optional<OrderItem> findByOrderAndBook(Order order, Book book);
}
