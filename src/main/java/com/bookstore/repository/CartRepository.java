package com.bookstore.repository;

import com.bookstore.model.Cart;
import com.bookstore.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {

    // Find the cart for a specific user
    Optional<Cart> findByUser(User user);
}