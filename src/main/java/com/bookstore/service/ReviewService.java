package com.bookstore.service;

import com.bookstore.model.Review;

import java.util.List;
import java.util.Optional;

public interface ReviewService {

    Review addReview(Long bookId, Long userId, Review review);

    Optional<Review> getReviewById(Long bookId);

    List<Review> getReviewsByBook(Long bookId);

    List<Review> getReviewsByUser(Long userId);

    Optional<Review> getReviewByBookAndUser(Long bookId, Long userId);

    Review updateReview(Long reviewId, Review updatedReview);

    void deleteReview(Long reviewId);
}
