package com.bookstore.service.impl;

import com.bookstore.exception.DuplicateResourceException;
import com.bookstore.exception.ResourceNotFoundException;
import com.bookstore.model.Book;
import com.bookstore.model.Review;
import com.bookstore.model.User;
import com.bookstore.repository.BookRepository;
import com.bookstore.repository.ReviewRepository;
import com.bookstore.repository.UserRepository;
import com.bookstore.service.ReviewService;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

@Service
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;
    private final BookRepository bookRepository;
    private final UserRepository userRepository;

    public ReviewServiceImpl(
            ReviewRepository reviewRepository,
            BookRepository bookRepository,
            UserRepository userRepository) {
        this.reviewRepository = reviewRepository;
        this.bookRepository = bookRepository;
        this.userRepository = userRepository;
    }

    @Override
    public Review addReview(Long bookId, Long userId, Review review) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found"));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        // Prevent duplicate review
        reviewRepository.findByBookAndUser(book, user)
                .ifPresent(r -> {
                    throw new DuplicateResourceException("User already reviewed this book");
                });

        review.setBook(book);
        review.setUser(user);

        return reviewRepository.save(review);
    }

    @Override
    public Optional<Review> getReviewById(Long bookId) {
        return reviewRepository.findById(bookId);
    }

    @Override
    public List<Review> getReviewsByBook(Long bookId) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found"));
        return reviewRepository.findByBook(book);
    }

    @Override
    public List<Review> getReviewsByUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        return reviewRepository.findByUser(user);
    }

    @Override
    public Optional<Review> getReviewByBookAndUser(Long bookId, Long userId) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found"));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        return reviewRepository.findByBookAndUser(book, user);
    }

    @Override
    public Review updateReview(Long reviewId, Review updatedReview) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ResourceNotFoundException("Review not found"));

        review.setComment(updatedReview.getComment());
        review.setRating(updatedReview.getRating());

        return reviewRepository.save(review);
    }

    @Override
    public void deleteReview(Long reviewId) {
        if (!reviewRepository.existsById(reviewId)) {
            throw new ResourceNotFoundException("Review not found");
        }
        reviewRepository.deleteById(reviewId);
    }
}
