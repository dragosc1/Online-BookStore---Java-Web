package com.bookstore.repository;

import com.bookstore.model.Review;
import com.bookstore.model.Book;
import com.bookstore.model.User;
import com.bookstore.model.enums.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

    // Find all reviews for a specific book
    List<Review> findByBook(Book book);

    // Find all reviews made by a specific user
    List<Review> findByUser(User user);

    // Find a review by a specific user for a specific book
    Optional<Review> findByBookAndUser(Book book, User user);
}
