package com.bookstore.repository;

import com.bookstore.model.Payment;
import com.bookstore.model.Order;
import com.bookstore.model.enums.PaymentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {

    // Find payment by order
    Optional<Payment> findByOrder(Order order);

    // Find all payments by status
    List<Payment> findByStatus(PaymentStatus status);

    // Optional: Find payment by order and status
    Optional<Payment> findByOrderAndStatus(Order order, PaymentStatus status);
}
