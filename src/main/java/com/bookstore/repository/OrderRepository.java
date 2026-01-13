package com.bookstore.repository;

import com.bookstore.model.Order;
import com.bookstore.model.User;
import com.bookstore.model.enums.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    // Find all orders for a specific user
    List<Order> findByUser(User user);

    // Find all orders with a specific status
    List<Order> findByStatus(OrderStatus status);

    // Find all orders for a specific user with a specific status
    List<Order> findByUserAndStatus(User user, OrderStatus status);

    @Query("""
        SELECT COUNT(o) > 0
        FROM Order o
        JOIN o.orderItems oi
        WHERE o.user.id = :userId
          AND oi.book.id = :bookId
          AND o.status = :status
    """)
    boolean existsDeliveredOrderWithBook(Long userId, Long bookId, OrderStatus status);
}
