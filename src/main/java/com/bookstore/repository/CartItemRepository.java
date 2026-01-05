package com.bookstore.repository;

import com.bookstore.model.Cart;
import com.bookstore.model.CartItem;
import com.bookstore.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    // Find all items in a specific cart
    List<CartItem> findByCart(Cart cart);

    // Find a specific cart item by cart and book
    Optional<CartItem> findByCartAndBook(Cart cart, Book book);

    // Optional: Find all cart items for a specific book
    List<CartItem> findByBook(Book book);
}
